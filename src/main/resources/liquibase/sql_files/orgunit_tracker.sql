--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: orgunit_tracker; Type: TABLE; Schema: public; Owner: analytics; Tablespace: 
--

CREATE TABLE orgunit_tracker (
    orgunit text NOT NULL,
    id text,
    date_created timestamp without time zone
);


ALTER TABLE public.orgunit_tracker OWNER TO analytics;

--
-- Data for Name: orgunit_tracker; Type: TABLE DATA; Schema: public; Owner: analytics
--

INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWARC-PARI', 'bQEvuAiqYo9', '2021-10-22 12:05:42');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_0946', 'BX6PxMvpFea', '2021-10-22 12:05:42');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_0732', 'Fntbi8rwatX', '2021-10-22 12:05:42');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_0410', 'yWnQdb1QYZz', '2021-10-22 12:05:42');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_0839', 'mDznG01Kedz', '2021-10-22 12:05:42');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_0840', 'Ol5BYPpj9Ls', '2021-10-22 12:05:42');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_0303', 'kAPbtmSmzox', '2021-10-22 12:05:42');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_01', 'zjiCmkAyHCv', '2021-10-22 12:05:42');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_0101', 'bq5CTZBbnss', '2021-10-22 12:05:42');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_0947', 'Ble4b4h5u7i', '2021-10-22 12:05:42');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWCB0845-XXX-01', 'qbgkjp267yC', '2021-10-22 12:05:42');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWCeSHHAR-BULAWAYO', 'Fwm6vpigZha', '2021-10-22 12:05:42');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWCeSHHAR-CHIPINGE', 'vjctP9ApVp5', '2021-10-22 12:05:42');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWCeSHHAR-GWERU', 'Du8CU71SBRj', '2021-10-22 12:05:42');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWCeSHHAR-HARARE', 'vKdA4yCPMoS', '2021-10-22 12:05:42');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWCeSHHAR-MASVINGO', 'Ku9npaiF7B2', '2021-10-22 12:05:42');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWCeSHHAR-MUTARE', 'e5JiMZ1ZXoM', '2021-10-22 12:05:42');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWCeSHHAR-MAKONI', 'nq0hCekq4hZ', '2021-10-22 12:05:42');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_0411', 'a8910fwJpjN', '2021-10-22 12:05:42');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_0626', 'dC1toPiWUjb', '2021-10-22 12:05:42');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_0517', 'cuVhZmua0JZ', '2021-10-22 12:05:42');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_0304', 'QACkzFO716H', '2021-10-22 12:05:42');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_0305', 'i1SqtcyGII3', '2021-10-22 12:05:42');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_0733', 'Nnf9W50Snnu', '2021-10-22 12:05:42');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_1053', 'EPcMBv8YjJZ', '2021-10-22 12:05:42');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_0734', 'KNzChJwHJ9z', '2021-10-22 12:05:42');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_101470', 'bOyWTTlyP4g', '2021-10-22 12:05:42');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWFST-Beitbridge', 'vPgW0FyYoIj', '2021-10-22 12:05:42');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWFST-CHITUNGWIZA', 'hB5sj2Urwei', '2021-10-22 12:05:42');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWFST-GWERU', 'tSHY83yELIu', '2021-10-22 12:05:42');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWFST-HARARE', 'ZSrMxyPO8Jo', '2021-10-22 12:05:42');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWFST-Mpilo', 'tftPepN7I8W', '2021-10-22 12:05:42');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWFST-MUTARE', 'elmi9DE0kKs', '2021-10-22 12:05:42');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_1054', 'jvhDNvawPB0', '2021-10-22 12:05:42');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_1055', 'SuHGG15RgFv', '2021-10-22 12:05:42');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_0518', 'ZYRtk7MBFN8', '2021-10-22 12:05:42');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_0412', 'NaFiLa2Z3yZ', '2021-10-22 12:05:42');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_0735', 'U56ygCJ4bqn', '2021-10-22 12:05:42');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_0948', 'ad5MparSPZP', '2021-10-22 12:05:42');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_1056', 'TA2Eeq90w5y', '2021-10-22 12:05:42');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_02', 'fM70QkC28FW', '2021-10-22 12:05:42');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_0202', 'hE7VmczIyRT', '2021-10-22 12:05:42');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_0627', 'QFk6ableDOG', '2021-10-22 12:05:42');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_0841', 'gRFXFaAaO83', '2021-10-22 12:05:42');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_0525', 'mpFJPTmSJ9N', '2021-10-22 12:05:42');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_138_79', 'qaRFQFdN7YO', '2021-10-22 12:05:42');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_138_09', 'TAO86CACege', '2021-10-22 12:05:42');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_138_10', 'HgE8si3v8Jn', '2021-10-22 12:05:42');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_138_11', 'iBZTyPmYU9w', '2021-10-22 12:05:42');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_138_60', 'o7NTzop72AV', '2021-10-22 12:05:42');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_138_04', 'Cuc67aCus8u', '2021-10-22 12:05:42');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_138_30', 'i9Tu9yg4PUw', '2021-10-22 12:05:42');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_138_71', 'c69RLC6uJTc', '2021-10-22 12:05:42');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_138_24', 'EN2wuuwUTao', '2021-10-22 12:05:42');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_138_27', 'k01ZRpbS1yJ', '2021-10-22 12:05:42');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_138_28', 'tFPIn9strzX', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_138_34', 'w9eq7Lkv8bJ', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_RUJ_03', 'lOJO2UCtgZc', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_138_33', 'YZEMQXQqN9X', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_RUJ_01', 'P36aZdmrQUq', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_138_40', 'acEziB6DWDT', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_138_18', 'vXNTJLrrp9E', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_138_13', 'sYka9WdxCpn', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_138_01', 'zcsLkUx5sGC', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_138_57', 'yMU40D6BWnq', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_138_19', 'HUNdpFqQ3Ll', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_138_06', 'p3b2C9Nfaif', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_138_17', 'p1YTHyiq4V6', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_138_07', 'QwG0pJkUgNu', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_138_32', 'MnChEjGjIxa', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_138_03', 'dydVwopir8S', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_138_72', 'NDcH41q7SCe', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_138_20', 'aNP3tULyMVK', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_138_12', 'f7xPQOrnxDT', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_138_31', 'VRbZfzScYp0', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_138_22', 'OBEKv0q4win', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_138_16', 'YihgupAToT1', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_138_42', 'O6Z3OVpIiRz', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_138_35', 'Yke9Y9Lm32y', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_RUJ_02', 'TvdE4F7GIXC', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_138_25', 'UrcKtYYu1pS', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_138_26', 'F0CMeOSg7Su', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_138_21', 'oNHpMQkrmVN', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_138_64', 'Ut2nAbTafMW', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_138_14', 'mHy1Sou7Lwu', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_138_08', 'vnfLdbQxjgo', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_138_15', 'sF1KUTVPeJH', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_138_23', 'BkxYvSjlxQI', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_138_29', 'r37bDsoESAz', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_138_02', 'HP7UvoGO9AH', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_138_73', 'fHyZazYmHbf', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_0949', 'i7PBHgEEkMb', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_0628', 'hcqIqFRZUnp', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_0629', 'b9x0KR2AbBr', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_1057', 'GFMBVKvUgE8', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_02LWS', 'YEEP3yFkdzp', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_0842', 'Whn0nBgrk7z', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_0630', 'S17fVYGr8xK', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_0306', 'Czn23jlfoKK', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_0950', 'BEfx5z13z87', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_03', 'XMhEvmviMgo', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_0519', 'wqX9AQ1sW47', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_04', 'vHSAW5b2nSX', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_05', 'yDU61sLwy6Y', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_06', 'krL3QeQ69wU', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_07', 'ev2DjMiAgcM', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_0736', 'X2UPOhVc4LL', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_08', 'oNKU4sVwr7g', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_09', 'FjavHDpivix', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_0951', 'lgtnX7tAQ35', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_0413', 'ra2JJ2PYL1r', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_1058', 'Twb1LohNNsm', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_10', 'g5vvjRv7Tl0', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_0414', 'q6U6eOOVJfc', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_0520', 'j7e0YtzvRUu', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_0521', 'atquNNqhgd4', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_0307', 'VAS2NDr3Ey2', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_0308', 'WQsJhiBXasP', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_0522', 'SKRULVGeWIp', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_0737', 'qyX261dF3rX', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-13', 'vTrKtqBD3xT', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-BAMBANANI', 'wI7limHiG8V', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-BINDURA', 'SL5kh8c9vYI', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-CHINHOYI', 'LyBIsYtAS5F', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-CHIPINGE', 'ACA6K5m61hO', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-CHIREDZI', 'v7b4046rqSo', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-CHITUNGWIZA', 'DoA0vsy8Da5', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-CONCESSION', 'bEiRjf39jW2', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-GU', 'rT1SE8HA4HH', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-GWERU', 'geFeb00Z7yu', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-MAC', 'KP8boYIVyI7', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-MASVINGO', 'y9LZSXLLa1U', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-MURAMBINDA', 'FcFVFawMtS6', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-MUTARE', 'wZNldcyMu82', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-NAH', 'Y3Vj2G53Dbi', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-NKULUMANE', 'MFoYKckz2av', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-TRIANGLE', 'PAT8fLkO2ks', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-BAMBANANI-BEITBRIDGE', 'IvgmS4XiFZ5', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-BAMBANANI-BINGA', 'XZJb1Kfs1Kb', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-BAMBANANI-BUBI', 'olQX1P6DqcL', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-BAMBANANI-BULAWAYO', 'nXCTPVleiHB', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-BAMBANANI-BULILIMA', 'keyYa6De6e5', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-BAMBANANI-GWANDA', 'RoTNNAjKzTU', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-BAMBANANI-INSIZA', 'p9BxsjKO19C', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-BAMBANANI-LUPANE', 'O4u1lJAgPoH', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-BAMBANANI-MATOBO', 'we21gOqJHHu', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-BAMBANANI-NKAYI', 'fR62dXzSENk', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-BAMBANANI-TSHOLOTSHO', 'O2y2FBzjJFC', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-BAMBANANI-UMGUZA', 'CCo4gjadk2l', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-BAMBANANI-UMZINGWANE', 'Tx9gBP9hCuU', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-BINDURA-BINDURA', 'trEGxbwrNlY', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-BINDURA-RUSHINGA', 'vxWKANtnPUT', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-BUHERA-BUHERA', 'Tc4uiNUIxyE', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-CHINHOYI-HURUNGWE', 'K576AGCWgYO', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-CHINHOYI-KADOMA', 'tCqsGIM5Sw3', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-CHINHOYI-KARIBA', 'oqYEOe8Hslr', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-CHINHOYI-MAKONDE', 'JCkzDtvLIEH', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-CHINHOYI-ZVIMBA', 't53DedTlc25', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-CHIPINGE-CHIMANIMANI', 'yaRBPd9RxnX', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-CHIPINGE-CHIPINGE', 'brDZ09hGECl', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-CHIREDZI-BIKITA', 'Xh3fVPvGItj', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-CHIREDZI-CHIREDZI', 'elgr5mNrDCs', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-CHIREDZI-CHIVI', 'MiW9SlWBwhN', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-CHIREDZI-MWENEZI', 'BEk0El67ccA', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-CHITUNGWIZA-CENTENARY', 'UsGtzrb9Wsw', '2021-10-22 12:05:43');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-CHITUNGWIZA-CHEGUTU', 'WhroEFT5kO4', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-CHITUNGWIZA-CHIKOMBA', 'b5vBtyjV5NP', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('NSC-OU-CHITUNGWIZA-CHITUNGWIZA', 'kMUQffsAm87', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-CHITUNGWIZA-GOROMONZI', 'T3nwkeRyjr4', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-CHITUNGWIZA-HARARE', 'TOXg6sf7ZWD', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-CHITUNGWIZA-HWEDZA', 'KN16a1jChbO', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-CHITUNGWIZA-MARONDERA', 'c9KjGgF2Ndu', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-CHITUNGWIZA-MUDZI', 'XcoQQylk1E9', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-CHITUNGWIZA-MUREHWA', 'AoetsUaGPfe', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-CHITUNGWIZA-MUTOKO', 'XDpemOna0g0', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-CHITUNGWIZA-SEKE', 'BMrZesGfNih', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-CHITUNGWIZA-BUHERA', 'q2thzEswbRV', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-CONCESSION-GURUVE', 'wOypizM55PS', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-CONCESSION-MAZOWE', 'VMGi2MuFA7E', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-NAH-MAZOWE', 'YMOCFJIOm5h', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-CONCESSION-MT DARWIN', 'dw8gjqovggm', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-GWERU-CHIRUMHANZU', 'jpGEgT0lxOO', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-GWERU-GOKWE SOUTH', 'myhZ2tIFVgd', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-GWERU-GWERU', 'AZXDoyGBXoj', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-GWERU-KWEKWE', 'F6i70yLy6m6', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-GWERU-MBERENGWA', 'xrKLnDSOl59', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-GWERU-MWENEZI', 'MenMStiSpoW', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-INSIZA-BEITBRIDGE', 'A6a9TcmsDXt', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-INSIZA -BEITBRIDGE', 'jSn3qhYZrts', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-INSIZA-GWANDA', 'CETYbGbzldg', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-INSIZA -GWANDA', 'GPkzDMbDKCC', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-INSIZA-INSIZA', 'CHMd3xos1IF', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-INSIZA -INSIZA', 'FU7t56qZIvW', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-INSIZA -UMZINGWANE', 'TGvyFMzK50z', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-INSIZA-UMZINGWANE', 'csMVepIKNqb', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-MAC-BULAWAYO', 'DQYAHsUcuPz', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-MAC-BULILIMA', 'gz4d4gthdd9', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-MAC-HWANGE', 'QSnbVsbNPiH', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-MAC-INSIZA', 'd5HNQbbLABQ', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-MAC-MANGWE', 'Vk1r3CU5Lcz', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-MAC-MATOBO', 'dFI9YC5Gobh', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-MAC-NKAYI', 'bJU5JK7xa76', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-MAC-BEITBRIDGE', 'ImXDf1RXHeU', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-MAC-GWANDA', 'pPFfuUPjOmq', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-MAC-UMZINGWANE', 'PWfXLimA9yo', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-MBERENGWA-MBERENGWA', 'U2wXl42A8G9', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-MAKONI-MAKONI', 'BPL3Lm5evIi', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-MASVINGO-BIKITA', 'olQvquqBztI', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-MASVINGO-CHIVI', 'kiZa0q9TaHX', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-MASVINGO-GUTU', 'iJ1BIzi1hYq', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-MASVINGO-MASVINGO', 'fHUPtuEfnfc', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-MASVINGO-MBERENGWA', 'xabHVYiYTTu', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-MASVINGO-MWENEZI', 'PruX3Rr6pj5', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-MASVINGO-SHURUGWI', 'RzXJbSIupSo', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-MASVINGO-ZAKA', 'PohEtd1JqRG', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-MASVINGO-ZVISHAVANE', 'A6MSVnHrxKQ', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-MURAMBINDA-BUHERA', 'f0GbZCk5Auq', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-MUTARE-MAKONI', 'W0M9edkGFNA', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-MUTARE-MUTARE', 'LR3o5TyrOSl', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-MUTARE-MUTASA', 'HtqWPxNdcK6', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-MUTARE-NYANGA', 'hpOIIsv6Uzz', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-MUTARE-BUHERA', 'CPZEYxsOtC6', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-NAH-CHEGUTU', 'eNyHFR1znjO', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-NAH-GOROMONZI', 'Z5DQV7m9Wpm', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-NAH-HARARE', 'aatiHqVi0RH', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-NAH-KADOMA', 'XSgDAQGp580', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-NAH-MUREHWA', 'nFqnvUMKiWG', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-NAH-SHAMVA', 'qvc7xFLEEh1', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-NAH-UMP', 'UTzxPsdngA6', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-NAH-ZVIMBA', 'h7VIYnOF4He', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-TRIANGLE-CHIREDZI', 'RQf8jUZKj6M', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_0843', 'WMI4xzpjYWj', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_0309', 'xyCfmjMlR0A', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_138_DFO', 'IpLnydP6wVw', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_138_55', 'b1gLiOnemK7', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_138_76', 'jfMc8NnJYGU', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_138_78', 'zGuQlfRxJsA', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_138_77', 'ZlafqPLEi3W', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_138_65', 'G3AyeGlN9eZ', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_138_66', 'KfTDSTF7YHy', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_138_70', 'H6FU8POrbof', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_138_68', 'GLyHzbRASEn', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_138_75', 'MvGOrkCOx7X', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_138_74', 'kFttrkbORpq', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_138_54', 'q8RR7Bf4Aiq', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_138_53', 'veVcL9qWa0j', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_138_69', 'K94D66naz9h', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_138_67', 'fEYN6cwKBeV', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_138_52', 'Fk03ONaA6Ll', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_138_05', 'KJKcxO9J5ci', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_RUJ_DFO', 'hD4aXs6zcJQ', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_138_51', 'bmZqR0RqGT2', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_138_80', 'RK5r3WGLOUu', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_138_50', 'FAMqJhHySKr', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW101469', 'qQJlSZ0hnbZ', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_0415', 'lttyL1Ov61v', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_0523', 'ccccBHUjKxy', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_0416', 'NAZJrYPkOxA', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_1059', 'ZyYKQSOwWHI', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_0844', 'ARP40wIhNw8', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_0524', 'dtHxWxeXuz5', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_0845', 'YYEXp0C12zw', '2021-10-22 12:05:44');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_0952', 'FQCmhHeXOI4', '2021-10-22 12:05:45');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW138', 'jByqdiPybUq', '2021-10-22 12:05:45');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-NAH-BUH ERA', 'nRgoNl2uc61', '2021-10-22 12:05:45');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZWNSC-OU-NAH-BUHERA', 'Uyy9TPybICD', '2021-10-22 12:05:45');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_0738', 'f5IkBLKR1xz', '2021-10-22 12:05:45');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW', 'TwUzWzgDAST', '2021-10-22 12:05:45');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_0631', 'jjY2UJTKwiu', '2021-10-22 12:05:45');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_1060', 'rwJSnzDxLRn', '2021-10-22 12:05:45');
INSERT INTO orgunit_tracker (orgunit, id, date_created) VALUES ('ZW_PSI', 'gFA2Ef0dm4N', '2021-10-22 12:05:45');


--
-- Name: orgunit_tracker_pkey; Type: CONSTRAINT; Schema: public; Owner: analytics; Tablespace: 
--

ALTER TABLE ONLY orgunit_tracker
    ADD CONSTRAINT orgunit_tracker_pkey PRIMARY KEY (orgunit);


--
-- PostgreSQL database dump complete
--

