package fi.vm.sade.kayttooikeus.service.impl;

import fi.vm.sade.kayttooikeus.config.properties.AuthProperties;
import fi.vm.sade.kayttooikeus.service.CryptoService;
import fi.vm.sade.kayttooikeus.service.exception.PasswordException;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Service
public class CryptoServiceImpl implements CryptoService {

    private static final int iterations =  2 * 1024;
    private static final int saltLen = 128;
    private static final int desiredKeyLen = 128;
    private static final String ALGORITHM = "PBKDF2WithHmacSHA1";

    private String STATIC_SALT;

    private static final Pattern forbidden = Pattern.compile("[^a-zA-Z0-9!#$%*_+=?]");
    private static final Pattern lowercase = Pattern.compile("[a-z]");
    private static final Pattern uppercase = Pattern.compile("[A-Z]");
    private static final Pattern special = Pattern.compile("[!#$%*_+=?]");
    private static final Pattern numbers = Pattern.compile("[0-9]");

    private Integer passwordMinLen;

    @Autowired
    public CryptoServiceImpl(AuthProperties authProperties) {
        this.STATIC_SALT = authProperties.getCryptoService().getStaticSalt();
        this.passwordMinLen = authProperties.getPassword().getMinLen();
    }

    /**
     * Computes a salted PBKDF2 hash of given plain text password suitable for
     * storing in a database.
     */
    @Override
    public String getSaltedHash(String password, String salt) {
        try {
            return hash(password, Base64.decodeBase64(combineSalt(salt)));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    private String combineSalt (String salt) {
        return STATIC_SALT + salt;
    }

    /**
     * Generates 128 long salt String, Base64 encoded
     */
    @Override
    public String generateSalt() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(saltLen, random).toString(32);
    }

    /**
     * Checks whether given plain text password corresponds to a stored salted
     * hash of the password.
     */
    @Override
    public boolean check(String password, String storedHash, String storedSalt) {
        String hashOfInput = this.getSaltedHash(password, storedSalt);
        return hashOfInput.equals(storedHash);
    }

    // using PBKDF2 from Sun, an alternative is https://github.com/wg/scrypt
    // cf. http://www.unlimitednovelty.com/2012/03/dont-use-bcrypt.html
    private String hash(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory f = SecretKeyFactory.getInstance(ALGORITHM);
        SecretKey key = f.generateSecret(new PBEKeySpec(password.toCharArray(), salt, iterations, desiredKeyLen));
        return Base64.encodeBase64String(key.getEncoded()).trim();
        //trim is needed to remove CRLF that base64Encode puts there
    }

    @Override
    public List<String> isStrongPassword(String password) {
        List<String> errors = new ArrayList<>();

        if ( isEmpty(password, errors)) {
            return errors;
        }

        password = password.trim();

        if ( hasForbiddenCharacters(password, errors) ) {
            return errors;
        }

        if ( isTooShort(password, errors)) {
            return errors;
        }

        if ( Stream.of(
                hasLowercase(password, errors),
                hasUppercase(password, errors),
                hasSpecial(password, errors),
                hasNumbers(password, errors)
        ).filter(a -> a).count() >= 3 ) {
            return Collections.EMPTY_LIST;
        }

        return errors;
    }

    private boolean isEmpty(String password, List<String> errors) {
        if (password == null) {
            errors.add("validPassword.empty");
        }
        return password == null;
    }

    private boolean isTooShort(String password, List<String> errors) {
        if (password.length() < passwordMinLen) {
            errors.add("validPassword.short;" + passwordMinLen);
        }
        return password.length() < passwordMinLen;
    }

    private boolean hasForbiddenCharacters(String password, List<String> errors) {
        if ( forbidden.matcher(password).find() ) {
            errors.add("validPassword.forbidden");
            return true;
        }
        return false;
    }

    private Boolean hasLowercase(String password, List<String> errors) {
        if ( lowercase.matcher(password).find() ) {
            return true;
        }
        errors.add("validPassword.lowercase");
        return false;
    }

    private Boolean hasUppercase(String password, List<String> errors) {
        if ( uppercase.matcher(password).find() ) {
            return true;
        }
        errors.add("validPassword.uppercase");
        return false;
    }

    private Boolean hasSpecial(String password, List<String> errors) {
        if ( special.matcher(password).find() ) {
            return true;
        }
        errors.add("validPassword.nospecial");
        return false;
    }

    private Boolean hasNumbers(String password, List<String> errors) {
        if ( numbers.matcher(password).find() ) {
            return true;
        }
        errors.add("validPassword.numbers");
        return false;
    }

    @Override
    public void throwIfNotStrongPassword(String password) {
        isStrongPassword(password)
                .stream().findFirst().ifPresent((error) -> {throw new PasswordException(error);});
    }
}
