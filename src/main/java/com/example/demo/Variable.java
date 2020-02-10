package com.example.demo;

public class Variable {

    //product state
    public static final long PSTATE_ONSALE = 0;
    public static final long PSTATE_OVERSALE = 1;
    public static final long PSTATE_ONTHEWAY = 2;
    public static final long PSTATE_READY = 3;
    public static final long PSTATE_DANDAN = 4;
    public static final long PSTATE_GUIYUAN = 5;

    //order state
    public static final long OSTATE_UNPAID = 0;
    public static final long OSTATE_SUBMITPRODUCTPAID = 1;
    public static final long OSTATE_PRODUCTPAID = 2;
    public static final long OSTATE_SUBMITFERIGHTPAID = 3;
    public static final long OSTATE_FERIGHTPAID = 4;
    public static final long OSTATE_SHIPMENT = 5;
    public static final long OSTATE_SUBMITREFUND = 6;
    public static final long OSTATE_REFUND = 7;

    //payment state
    public static final long PASTATE_UNPAID = 0;
    public static final long PASTATE_PAYFAIL = 1;
    public static final long PASTATE_PAID = 2;
    public static final long PASTATE_DANDAN = 3;
    public static final long PASTATE_GUIYUAN = 4;

    //payment type
    public static final long PASTYPE_PRODUCT = 0;
    public static final long PASTYLE_FREGHT = 1;

}
