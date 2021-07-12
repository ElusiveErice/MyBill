package com.eric.mybill.database;

public class DbSchema {

    public static final class BillTable{
        public static final String NAME = "bills";

        public static final class Cols{
            public static final String TIMES = "times";
            public static final String UUID = "uuid";
            public static final String YEAR = "year";
            public static final String MONTH = "month";
            public static final String DAY = "day";
            public static final String OIL_CARD = "oil_card";
            public static final String DEPARTURE = "departure";
            public static final String DESTINATION = "destination";
            public static final String GOOD_TYPE = "good_type";
            public static final String PRICE = "price";
            public static final String WEIGHT = "weight";
            public static final String TOTAL_PRICE = "total_price";
            public static final String NOTES = "notes";
            public static final String TOTAL_PRICE_SOLVE = "total_price_solve";
            public static final String OIL_CARD_SOLVE = "oil_card_solve";
        }
    }

    public static final class BillRankTable{
        public static final String NAME = "bill_rank";

        public static final class Cols{
            public static final String UUID = "uuid";
            public static final String YEAR = "year";
            public static final String MONTH = "month";
            public static final String DAY = "day";
            public static final String FORM = "form";
        }
    }
    public static final class MaintenanceTable{
        public static final String NAME = "maintenance";

        public static final class Cols{
            public static final String UUID = "uuid";
            public static final String YEAR = "year";
            public static final String MONTH = "month";
            public static final String DAY = "day";
            public static final String NAME = "name";
            public static final String TOTAL_PRICE = "total_price";
            public static final String NOTES = "notes";
        }
    }
}
