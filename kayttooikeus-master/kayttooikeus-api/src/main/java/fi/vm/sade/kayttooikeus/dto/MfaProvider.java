package fi.vm.sade.kayttooikeus.dto;

public enum MfaProvider {
  GAUTH("mfa-gauth");

  private String mfaProvider;

  MfaProvider(String mfaProvider) {
    this.mfaProvider = mfaProvider;
  }

  public String getMfaProvider() {
    return mfaProvider;
  }
}
