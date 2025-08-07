package com.openmock.oscaroscrapper;

public class OscaroScrapperFactory {

    private static OscaroScrapperFactory instance;

    private OscaroScrapperFactory(){

    }

    public static OscaroScrapperFactory getInstance(){
        if(instance == null){
            instance = new OscaroScrapperFactory();
        }

        return instance;
    }

    public AbstractOscaroScrapper getOscaroScrapper(OscaroScrapperType type, String lang){
        AbstractOscaroScrapper scrapper = null;
        switch (type){
            case DEFAULT -> scrapper = new DefaultOscaroScrapper(lang);
            case ZEN_ROW -> scrapper = new ZenRowsOscaroScrapper(lang);
        }

        return scrapper;
    }
}
