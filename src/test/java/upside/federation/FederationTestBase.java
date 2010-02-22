package upside.federation;

public class FederationTestBase {

    protected Config loadConf() {
        return new ConfigParser(getClass().getResourceAsStream("/test-conf.xml")).parse();
    }
}
