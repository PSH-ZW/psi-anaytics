package com.nuchange.psianalytics.constants;

public class MRSConstants {
    public enum ConceptDatatype {
        NA("N/A"),
        NUMERIC("Numeric"),
        CODED("Coded"),
        QUANTIFIER("Quantifier"),
        COMPLEX("Complex"),
        BOOLEAN("Boolean"),
        DRUG_FORM("Drug form"),
        DRUG("Drug"),
        DATE("Date"),
        DATETIME("Datetime"),
        TIME("Time"),
        TEXT("Text"),
        MULTI_SELECT("MultiSelect"),
        STRUCTURED_NUMERIC("Structured Numeric");
        private final String datatype;
        ConceptDatatype(String datatype) {
            this.datatype = datatype;
        }

        @Override
        public String toString() {
            return datatype;
        }

        public static ConceptDatatype create (String value) {
            if(value == null) {
                throw new IllegalArgumentException();
            }
            for(ConceptDatatype v : values()) {
                if(value.equals(v.datatype)) {
                    return v;
                }
            }
            throw new IllegalArgumentException();
        }
    }
}
