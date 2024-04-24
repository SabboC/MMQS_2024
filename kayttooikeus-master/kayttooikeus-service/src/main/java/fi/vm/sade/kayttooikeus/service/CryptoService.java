package fi.vm.sade.kayttooikeus.service;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface CryptoService {

    /**
     *
     * Generates 256 characters long hash from password and salt
     *
     * @param password
     * @param salt
     * @return
     * @throws Exception
     */
    String getSaltedHash(String password, String salt);

    /**
     *
     * Checks whether password matches salt and hash
     *
     * @param password
     * @param storedHash
     * @param storedSalt
     * @return
     * @throws Exception
     */
    boolean check(String password, String storedHash, String storedSalt);

    /**
     * Returns Base64 encoded salt string, 128 characters long
     *
     * @return
     * @throws NoSuchAlgorithmException
     */
    String generateSalt();

    List<String> isStrongPassword(String password);

    void throwIfNotStrongPassword(String password);
}
