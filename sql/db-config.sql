DROP TABLE "chocolates" CASCADE CONSTRAINTS;

DROP TABLE "ingredients" CASCADE CONSTRAINTS;

DROP TABLE "ingredient_usage_in_chocolate" CASCADE CONSTRAINTS;

DROP TABLE "ingredient_orders" CASCADE CONSTRAINTS;

DROP TABLE "clients" CASCADE CONSTRAINTS;

DROP TABLE "chocolate_orders" CASCADE CONSTRAINTS;

DROP TABLE "chocolate_order_entry" CASCADE CONSTRAINTS;

DROP TABLE "employees" CASCADE CONSTRAINTS;

DROP TABLE "work_log" CASCADE CONSTRAINTS;

DROP SEQUENCE "chocolates_sq";

DROP SEQUENCE "ingredients_sq";

DROP SEQUENCE "ingredient_orders_sq";

DROP SEQUENCE "clients_sq";

DROP SEQUENCE "chocolate_orders_sq";

DROP SEQUENCE "employees_sq";

/*--------- views ---------------*/

CREATE OR REPLACE FORCE VIEW OPEN_CHOCOLATE_ORDERS_VIEW
            (
             "order_id",
             "order_date",
             "client_id",
             "client_name"
                )
AS
SELECT o."id",
       o."date",
       o."client_id",
       c."name"
FROM "chocolate_orders" o,
     "clients" c
WHERE o."client_id" = c."id"
  AND o."status" = 0
ORDER BY o."date";

CREATE OR REPLACE FORCE VIEW CLOSED_CHOCOLATE_ORDERS_VIEW
            (
             "order_id",
             "order_date",
             "client_id",
             "client_name"
                )
AS
SELECT o."id",
       o."date",
       o."client_id",
       c."name"
FROM "chocolate_orders" o,
     "clients" c
WHERE o."client_id" = c."id"
  AND o."status" = 1
ORDER BY o."date";

CREATE OR REPLACE FORCE VIEW CHOCOLATE_ORDERS_ENTRY_VIEW
            (
             "order_id",
             "chocolate_id",
             "chocolate_name",
             "amount_wanted",
            "amount_available"
                )
AS
SELECT e."order_id",
       e."chocolate_id",
       c."name",
       e."amount",
       c."amount"
FROM "chocolate_order_entry" e,
     "chocolates" c
WHERE e."chocolate_id" = c."id";

CREATE OR REPLACE FORCE VIEW INGREDIENTS_ORDERS_VIEW
            (
             "order_id",
             "order_date",
             "ingredient_id",
             "ingredient_name",
             "amount",
            "status"
                )
AS
SELECT o."id",
       o."date",
       o."ingredient_id",
       i."name",
       o."amount",
       o."status"
FROM "ingredient_orders" o,
     "ingredients" i
WHERE o."ingredient_id" = i."id"
ORDER BY o."date";

CREATE OR REPLACE FORCE VIEW INGREDIENT_IN_CHOCOLATE_VIEW
            (
             "chocolate_id",
             "ingredient_id",
             "ingredient_name",
             "amount"
                )
AS
SELECT iu."chocolate_id",
       iu."ingredient_id",
       i."name",
       iu."amount"
FROM "ingredient_usage_in_chocolate" iu,
     "ingredients" i
WHERE iu."ingredient_id" = i."id";

/*--------- chocolates ---------------*/

CREATE TABLE "chocolates"
(
    "id"                 INTEGER      NOT NULL,
    "name"               VARCHAR2(50) NOT NULL,
    "sell_price_dollars" REAL         NOT NULL,
    "amount"             INTEGER      NOT NULL
);

ALTER TABLE "chocolates"
    ADD (
        CONSTRAINT "chocolates_pk" PRIMARY KEY ("id")
        );

CREATE SEQUENCE "chocolates_sq" START WITH 1;

CREATE
    OR REPLACE TRIGGER "chocolates_id_trig"
    BEFORE
        INSERT
    ON "chocolates"
    FOR EACH ROW
BEGIN
    SELECT "chocolates_sq".NEXTVAL
    INTO :new."id"
    FROM dual;

END;
/

/*--------- ingredients ---------------*/

CREATE TABLE "ingredients"
(
    "id"                    INTEGER      NOT NULL,
    "name"                  VARCHAR2(50) NOT NULL,
    "price"                REAL         NOT NULL,
    "amount"                NUMBER         NOT NULL
);

ALTER TABLE "ingredients"
    ADD (
        CONSTRAINT "ingredients_pk" PRIMARY KEY ("id")
        );

CREATE SEQUENCE "ingredients_sq" START WITH 1;

CREATE
    OR REPLACE TRIGGER "ingredients_id_trig"
    BEFORE
        INSERT
    ON "ingredients"
    FOR EACH ROW
BEGIN
    SELECT "ingredients_sq".NEXTVAL
    INTO :new."id"
    FROM dual;

END;
/

/*--------- ingredient_usage_in_chocolate ---------------*/

CREATE TABLE "ingredient_usage_in_chocolate"
(
    "chocolate_id"  INTEGER NOT NULL,
    "ingredient_id" INTEGER NOT NULL,
    "amount" INTEGER NOT NULL
);

ALTER TABLE "ingredient_usage_in_chocolate"
    ADD (
        CONSTRAINT "ingredient_usage_in_chocolate_chocolate_id" FOREIGN KEY ("chocolate_id")
            REFERENCES "chocolates" ("id")
                ON DELETE CASCADE,
        CONSTRAINT "ingredient_usage_in_chocolate_ingredient_id" FOREIGN KEY ("ingredient_id")
            REFERENCES "ingredients" ("id")
                ON DELETE CASCADE
        );

/*--------- ingredient_orders ---------------*/

CREATE TABLE "ingredient_orders"
(
    "id"            INTEGER NOT NULL,
    "date"          DATE    NOT NULL,
    "amount"        INTEGER NOT NULL,
    "status"        INTEGER NOT NULL,
    "ingredient_id" INTEGER NOT NULL
);

ALTER TABLE "ingredient_orders"
    ADD (
        CONSTRAINT "ingredient_orders_pk" PRIMARY KEY ("id")
        );

CREATE SEQUENCE "ingredient_orders_sq" START WITH 1;

CREATE
    OR REPLACE TRIGGER "ingredient_orders_id_trig"
    BEFORE
        INSERT
    ON "ingredient_orders"
    FOR EACH ROW
BEGIN
    SELECT "ingredient_orders_sq".NEXTVAL
    INTO :new."id"
    FROM dual;

END;
/

/*--------- clients ---------------*/

CREATE TABLE "clients"
(
    "id"   INTEGER      NOT NULL,
    "name" VARCHAR2(50) NOT NULL
);

ALTER TABLE "clients"
    ADD (
        CONSTRAINT "clients_pk" PRIMARY KEY ("id")
        );

CREATE SEQUENCE "clients_sq" START WITH 1;

CREATE
    OR REPLACE TRIGGER "clients_id_trig"
    BEFORE
        INSERT
    ON "clients"
    FOR EACH ROW
BEGIN
    SELECT "clients_sq".NEXTVAL
    INTO :new."id"
    FROM dual;

END;
/

/*--------- chocolate_orders ---------------*/

CREATE TABLE "chocolate_orders"
(
    "id"        INTEGER NOT NULL,
    "date"      DATE    NOT NULL,
    "client_id" INTEGER NOT NULL,
    "status"    INTEGER NOT NULL
);

ALTER TABLE "chocolate_orders"
    ADD (
        CONSTRAINT "chocolate_orders_pk" PRIMARY KEY ("id"),
        CONSTRAINT "chocolate_orders_client_id" FOREIGN KEY ("client_id")
            REFERENCES "clients" ("id")
                ON DELETE CASCADE
        );

CREATE SEQUENCE "chocolate_orders_sq" START WITH 1;

CREATE
    OR REPLACE TRIGGER "chocolate_orders_id_trig"
    BEFORE
        INSERT
    ON "chocolate_orders"
    FOR EACH ROW
BEGIN
    SELECT "chocolate_orders_sq".NEXTVAL
    INTO :new."id"
    FROM dual;

END;
/

/*--------- chocolate_order_entry ---------------*/

CREATE TABLE "chocolate_order_entry"
(
    "chocolate_id" INTEGER NOT NULL,
    "order_id"     INTEGER NOT NULL,
    "amount"       INTEGER NOT NULL
);

ALTER TABLE "chocolate_order_entry"
    ADD (
        CONSTRAINT "chocolate_order_entry_chocolate_id" FOREIGN KEY ("chocolate_id")
            REFERENCES "chocolates" ("id")
                ON DELETE CASCADE,
        CONSTRAINT "chocolate_order_entry_order_id" FOREIGN KEY ("order_id")
            REFERENCES "chocolate_orders" ("id")
                ON DELETE CASCADE
        );

/*--------- employees ---------------*/

CREATE TABLE "employees"
(
    "employee_id"   INTEGER      NOT NULL,
    "employee_name" VARCHAR2(50) NOT NULL,
    "salary"        REAL
);

ALTER TABLE "employees"
    ADD (
        CONSTRAINT employees_pk PRIMARY KEY ("employee_id")
        );

CREATE SEQUENCE "employees_sq" START WITH 1;

CREATE
    OR REPLACE TRIGGER "employees_id_trig"
    BEFORE
        INSERT
    ON "employees"
    FOR EACH ROW
BEGIN
    SELECT "employees_sq".NEXTVAL
    INTO :new."employee_id"
    FROM dual;

END;
/

/*--------- work_log ---------------*/

CREATE TABLE "work_log"
(
    "employee_id" INTEGER   NOT NULL,
    "clock_in"    TIMESTAMP NOT NULL,
    "clock_out"   TIMESTAMP NOT NULL
);

ALTER TABLE "work_log"
    ADD (
        CONSTRAINT "work_log_pk" FOREIGN KEY ("employee_id")
            REFERENCES "employees" ("employee_id")
                ON DELETE CASCADE
        );

/*--------- populate db ---------------*/

INSERT
    ALL
    INTO "chocolates"("name", "sell_price_dollars", "amount")
VALUES ('Semisweeet Chocolate', 12, 300)
INTO "chocolates"("name", "sell_price_dollars", "amount")
VALUES ('Bittersweet Chocolate', 13, 200)
INTO "chocolates"("name", "sell_price_dollars", "amount")
VALUES ('Unsweetened Chocolate', 14, 500)
INTO "chocolates"("name", "sell_price_dollars", "amount")
VALUES ('Cocoa Powder', 15, 700)
INTO "chocolates"("name", "sell_price_dollars", "amount")
VALUES ('White Chocolate', 7, 800)
INTO "chocolates"("name", "sell_price_dollars", "amount")
VALUES ('Milk Chocolate', 10, 1200)
INTO "chocolates"("name", "sell_price_dollars", "amount")
VALUES ('Ruby Chocolate', 9, 400)
INTO "chocolates"("name", "sell_price_dollars", "amount")
VALUES ('Couverture Chocolate', 18, 100)
INTO "chocolates"("name", "sell_price_dollars", "amount")
VALUES ('Dark Milk Chocolate', 8, 600)
INTO "chocolates"("name", "sell_price_dollars", "amount")
VALUES ('Chocolate Baking Chips', 7, 1100)
SELECT 1
FROM dual;

INSERT
    ALL
    INTO "employees" ("employee_name", "salary")
VALUES ('Alex', 8000)
INTO "employees" ("employee_name", "salary")
VALUES ('Tom', 8000)
INTO "employees" ("employee_name", "salary")
VALUES ('David', 3000)
INTO "employees" ("employee_name", "salary")
VALUES ('Eli', 4000)
INTO "employees" ("employee_name", "salary")
VALUES ('Hana', 2000)
INTO "employees" ("employee_name", "salary")
VALUES ('May', 1000)
INTO "employees" ("employee_name", "salary")
VALUES ('Luna', 500)
INTO "employees" ("employee_name", "salary")
VALUES ('Noy', 1000)
INTO "employees" ("employee_name", "salary")
VALUES ('Bin', 1000)
INTO "employees" ("employee_name", "salary")
VALUES ('Avi', 1000)
SELECT 1
FROM dual;

INSERT
    ALL
    INTO "clients" ("name")
VALUES ('Alexa')
INTO "clients" ("name")
VALUES ('Tomy')
INTO "clients" ("name")
VALUES ('Lula')
INTO "clients" ("name")
VALUES ('Tuna')
INTO "clients" ("name")
VALUES ('Lili')
INTO "clients" ("name")
VALUES ('Tony')
INTO "clients" ("name")
VALUES ('Maly')
INTO "clients" ("name")
VALUES ('Aviel')
INTO "clients" ("name")
VALUES ('Billy')
INTO "clients" ("name")
VALUES ('Barak')
SELECT 1
FROM dual;

INSERT
    ALL
    INTO "ingredients" ("name", "price", "amount")
VALUES ('Cocoa Butter', 2, 0)
INTO "ingredients" ("name", "price", "amount")
VALUES ('Coconut oil', 2, 12)
INTO "ingredients" ("name", "price", "amount")
VALUES ('Suger', 1, 32)
INTO "ingredients" ("name", "price", "amount")
VALUES ('Vanillin', 3, 42)
INTO "ingredients" ("name", "price", "amount")
VALUES ('Salt', 1, 4)
INTO "ingredients" ("name", "price", "amount")
VALUES ('Milk', 2, 2)
INTO "ingredients" ("name", "price", "amount")
VALUES ('Cream', 5, 11)
INTO "ingredients" ("name", "price", "amount")
VALUES ('Powdered Sugar', 2, 5)
INTO "ingredients" ("name", "price", "amount")
VALUES ('Canola oil', 2, 10)
INTO "ingredients" ("name", "price", "amount")
VALUES ('Baking Soda', 1, 5)
INTO "ingredients" ("name", "price", "amount")
VALUES ('Honey', 1, 23)
SELECT 1
FROM dual;

INSERT
    ALL
    INTO "ingredient_orders" ("date", "amount", "status", "ingredient_id")
VALUES (TO_DATE('12/08/2022', 'DD-MM-YYYY'), 500, 1, 1)
INTO "ingredient_orders" ("date", "amount", "status", "ingredient_id")
VALUES (TO_DATE('13/08/2022', 'DD-MM-YYYY'), 100, 1, 2)
INTO "ingredient_orders" ("date", "amount", "status", "ingredient_id")
VALUES (TO_DATE('14/08/2022', 'DD-MM-YYYY'), 150, 1, 3)
INTO "ingredient_orders" ("date", "amount", "status", "ingredient_id")
VALUES (TO_DATE('15/08/2022', 'DD-MM-YYYY'), 400, 1, 4)
INTO "ingredient_orders" ("date", "amount", "status", "ingredient_id")
VALUES (TO_DATE('16/08/2022', 'DD-MM-YYYY'), 100, 1, 5)
INTO "ingredient_orders" ("date", "amount", "status", "ingredient_id")
VALUES (TO_DATE('17/08/2022', 'DD-MM-YYYY'), 200, 1, 6)
INTO "ingredient_orders" ("date", "amount", "status", "ingredient_id")
VALUES (TO_DATE('18/08/2022', 'DD-MM-YYYY'), 150, 1, 7)
INTO "ingredient_orders" ("date", "amount", "status", "ingredient_id")
VALUES (TO_DATE('19/08/2022', 'DD-MM-YYYY'), 500, 0, 8)
INTO "ingredient_orders" ("date", "amount", "status", "ingredient_id")
VALUES (TO_DATE('20/08/2022', 'DD-MM-YYYY'), 600, 1, 9)
INTO "ingredient_orders" ("date", "amount", "status", "ingredient_id")
VALUES (TO_DATE('21/08/2022', 'DD-MM-YYYY'), 700, 1, 10)
SELECT 1
FROM dual;

INSERT
    ALL
    INTO "chocolate_orders" ("date", "client_id", "status")
VALUES (TO_DATE('12/08/2022', 'DD-MM-YYYY'), 1, 0)
INTO "chocolate_orders" ("date", "client_id", "status")
VALUES (TO_DATE('13/08/2022', 'DD-MM-YYYY'), 2, 1)
INTO "chocolate_orders" ("date", "client_id", "status")
VALUES (TO_DATE('14/08/2022', 'DD-MM-YYYY'), 3, 0)
INTO "chocolate_orders" ("date", "client_id", "status")
VALUES (TO_DATE('15/08/2022', 'DD-MM-YYYY'), 4, 1)
INTO "chocolate_orders" ("date", "client_id", "status")
VALUES (TO_DATE('16/08/2022', 'DD-MM-YYYY'), 5, 1)
INTO "chocolate_orders" ("date", "client_id", "status")
VALUES (TO_DATE('17/08/2022', 'DD-MM-YYYY'), 6, 0)
INTO "chocolate_orders" ("date", "client_id", "status")
VALUES (TO_DATE('18/08/2022', 'DD-MM-YYYY'), 7, 0)
INTO "chocolate_orders" ("date", "client_id", "status")
VALUES (TO_DATE('19/08/2022', 'DD-MM-YYYY'), 8, 1)
INTO "chocolate_orders" ("date", "client_id", "status")
VALUES (TO_DATE('20/08/2022', 'DD-MM-YYYY'), 9, 1)
INTO "chocolate_orders" ("date", "client_id", "status")
VALUES (TO_DATE('21/08/2022', 'DD-MM-YYYY'), 10, 0)
SELECT 1
FROM dual;

INSERT
    ALL
    INTO "chocolate_order_entry" ("chocolate_id", "order_id", "amount")
VALUES (1, 1, 100)
INTO "chocolate_order_entry" ("chocolate_id", "order_id", "amount")
VALUES (2, 2, 50)
INTO "chocolate_order_entry" ("chocolate_id", "order_id", "amount")
VALUES (3, 3, 70)
INTO "chocolate_order_entry" ("chocolate_id", "order_id", "amount")
VALUES (4, 4, 60)
INTO "chocolate_order_entry" ("chocolate_id", "order_id", "amount")
VALUES (5, 5, 80)
INTO "chocolate_order_entry" ("chocolate_id", "order_id", "amount")
VALUES (6, 6, 77)
INTO "chocolate_order_entry" ("chocolate_id", "order_id", "amount")
VALUES (7, 7, 66)
INTO "chocolate_order_entry" ("chocolate_id", "order_id", "amount")
VALUES (8, 8, 88)
INTO "chocolate_order_entry" ("chocolate_id", "order_id", "amount")
VALUES (9, 9, 99)
INTO "chocolate_order_entry" ("chocolate_id", "order_id", "amount")
VALUES (10, 10, 110)
SELECT 1
FROM dual;

INSERT
    ALL
    INTO "ingredient_usage_in_chocolate" ("chocolate_id", "ingredient_id", "amount")
VALUES (1, 1, 1)
INTO "ingredient_usage_in_chocolate" ("chocolate_id", "ingredient_id", "amount")
VALUES (2, 2, 1)
INTO "ingredient_usage_in_chocolate" ("chocolate_id", "ingredient_id", "amount")
VALUES (3, 3, 5)
INTO "ingredient_usage_in_chocolate" ("chocolate_id", "ingredient_id", "amount")
VALUES (4, 4, 2)
INTO "ingredient_usage_in_chocolate" ("chocolate_id", "ingredient_id", "amount")
VALUES (5, 5, 10)
INTO "ingredient_usage_in_chocolate" ("chocolate_id", "ingredient_id", "amount")
VALUES (6, 6, 2)
INTO "ingredient_usage_in_chocolate" ("chocolate_id", "ingredient_id", "amount")
VALUES (7, 7, 3)
INTO "ingredient_usage_in_chocolate" ("chocolate_id", "ingredient_id", "amount")
VALUES (8, 8, 1)
INTO "ingredient_usage_in_chocolate" ("chocolate_id", "ingredient_id", "amount")
VALUES (9, 9, 4)
INTO "ingredient_usage_in_chocolate" ("chocolate_id", "ingredient_id", "amount")
VALUES (10, 10, 1)
SELECT 1
FROM dual;



INSERT
    ALL
    INTO "work_log" ("employee_id", "clock_in", "clock_out")
VALUES (1, to_date('13/08/2022 08:00:00 AM', 'dd/mm/yyyy hh:mi:ss AM'),
        to_date('13/08/2022 10:00:00 PM', 'dd/mm/yyyy hh:mi:ss PM'))
INTO "work_log"("employee_id", "clock_in", "clock_out")
VALUES (2, to_date('13/08/2022 08:00:00 AM', 'dd/mm/yyyy hh:mi:ss AM'),
        to_date('13/08/2022 10:00:00 PM', 'dd/mm/yyyy hh:mi:ss PM'))
SELECT 1
FROM dual;
/*--------- procedures ---------------*/
CREATE OR REPLACE PACKAGE "procedures_pkg" AS
    PROCEDURE "open_new_chocolate_order"(
        porderdate DATE,
        pclientid INTEGER,
        porderno OUT INTEGER
    );
    PROCEDURE "add_new_chocolate_order_entry"(
        porderid INTEGER,
        pchocolateid INTEGER,
        pamount INTEGER
    );
    PROCEDURE "open_new_ingredient_order"(
        pingredientid INTEGER,
        porderdate DATE,
        pamount INTEGER,
        porderno OUT INTEGER
    );
    PROCEDURE "calc_working_time"(
        pemployeeid INTEGER,
        pmonth INTEGER,
        pyear INTEGER,
        empwlhours OUT NUMBER
    );
    PROCEDURE "mark_ingredient_order_complete"(
        porderno INTEGER
    );
    PROCEDURE "mark_chocolate_order_complete"(
        porderno INTEGER
    );
    PROCEDURE "delete_ingredient_from_chocolate"(
        pchocolateid INTEGER,
        pingredientid INTEGER
    );
    PROCEDURE "add_employee"(
        pemployeename VARCHAR2,
        pjobposition VARCHAR2
    );
END "procedures_pkg";
/

CREATE OR REPLACE PACKAGE BODY "procedures_pkg"
AS
    e_bad_argument EXCEPTION;
    PRAGMA EXCEPTION_INIT (e_bad_argument, -100000);
    e_missing_argument EXCEPTION;
    PRAGMA EXCEPTION_INIT (e_missing_argument, -100001);
    e_no_such_row EXCEPTION;
    PRAGMA EXCEPTION_INIT (e_no_such_row, -100002);
    e_not_enough_to_fulfill EXCEPTION;
    PRAGMA EXCEPTION_INIT (e_not_enough_to_fulfill, -100003);
    e_order_already_complete EXCEPTION;
    PRAGMA EXCEPTION_INIT (e_order_already_complete, -100004);
    e_bad_employee_position EXCEPTION;
    PRAGMA EXCEPTION_INIT (e_bad_employee_position, -100005);

    PROCEDURE "open_new_chocolate_order"(
        porderdate DATE,
        pclientid INTEGER,
        porderno OUT INTEGER
    ) IS
        v_i    NUMBER := 0;
    BEGIN
        IF porderdate IS NULL OR pclientid IS NULL THEN
            RAISE e_missing_argument;
        END IF;

        BEGIN
            SELECT 1 INTO v_i FROM "clients" WHERE "id" = pclientid;

            INSERT INTO "chocolate_orders" ("date", "client_id", "status")
            VALUES (porderdate, pclientid, 0);

            SELECT "chocolate_orders_sq".CURRVAL
            INTO v_i
            FROM dual;
        EXCEPTION
            WHEN no_data_found THEN
                RAISE e_no_such_row;
            WHEN OTHERS THEN
                RAISE;
        END;

        porderno := v_i;
    END "open_new_chocolate_order";

    PROCEDURE "add_new_chocolate_order_entry"(
        porderid INTEGER,
        pchocolateid INTEGER,
        pamount INTEGER
    ) IS

        v_i NUMBER := 0;
    BEGIN
        IF porderid IS NULL OR pchocolateid IS NULL OR pamount IS NULL THEN
            RAISE e_missing_argument;
        END IF;
        IF pamount <= 0 THEN
            RAISE e_bad_argument;
        END IF;

        BEGIN
            SELECT 1 INTO v_i FROM "chocolate_orders" WHERE "id" = porderid;
            SELECT 1 INTO v_i FROM "chocolates" WHERE "id" = pchocolateid;

            INSERT INTO "chocolate_order_entry" ("order_id", "chocolate_id", "amount")
            VALUES (porderid, pchocolateid, pamount);
        EXCEPTION
            WHEN no_data_found THEN
                RAISE e_no_such_row;
            WHEN OTHERS THEN
                RAISE;
        END;
    END "add_new_chocolate_order_entry";

    PROCEDURE "open_new_ingredient_order"(
        pingredientid INTEGER,
        porderdate DATE,
        pamount INTEGER,
        porderno OUT INTEGER
    ) IS

        v_i    NUMBER := 0;
    BEGIN
        IF pingredientid IS NULL OR porderdate IS NULL OR pamount IS NULL THEN
            RAISE e_missing_argument;
        END IF;
        IF pamount <= 0 THEN
            RAISE e_bad_argument;
        END IF;

        BEGIN
            SELECT 1 INTO v_i FROM "ingredients" WHERE "id" = pingredientid;

            INSERT INTO "ingredient_orders" ("amount", "date", "ingredient_id", "status")
            VALUES (pamount, porderdate, pingredientid, 0);

            SELECT "ingredient_orders_sq".CURRVAL
            INTO v_i
            FROM dual;
        EXCEPTION
            WHEN no_data_found THEN
                RAISE e_no_such_row;
            WHEN OTHERS THEN
                RAISE;
        END;

        porderno := v_i;
    END "open_new_ingredient_order";

    PROCEDURE "calc_working_time"(
        pemployeeid INTEGER,
        pmonth INTEGER,
        pyear INTEGER,
        empwlhours OUT NUMBER
    ) IS
        wlcalc  NUMBER := 0;
        var_in  "work_log"."clock_in"%TYPE;
        var_out "work_log"."clock_out"%TYPE;
        CURSOR emp_wl_cursor IS
            SELECT "clock_in",
                   "clock_out"
            FROM "work_log"
            WHERE "employee_id" = pemployeeid
              AND pmonth = EXTRACT(MONTH FROM "clock_in")
              AND pyear = EXTRACT(YEAR FROM "clock_out")
              AND "clock_out" IS NOT NULL;

    BEGIN
        OPEN emp_wl_cursor;
        LOOP
            FETCH emp_wl_cursor INTO
                var_in,
                var_out;
            EXIT WHEN emp_wl_cursor%notfound;
            wlcalc := wlcalc + EXTRACT(HOUR FROM (var_out - var_in));
        END LOOP;
        CLOSE emp_wl_cursor;
        empwlhours := wlcalc;
    END "calc_working_time";

    PROCEDURE "mark_ingredient_order_complete"(
        porderno INTEGER
    ) IS
        v_order_amount NUMBER := 0;
        v_i NUMBER := 0;
    BEGIN
        IF porderno IS NULL THEN
            RAISE e_missing_argument;
        END IF;

        BEGIN
            SELECT "amount", "status" INTO v_order_amount, v_i FROM "ingredient_orders"
                                                               WHERE "id" = porderno;
            IF v_i = 1 THEN
                RAISE e_order_already_complete;
            END IF;

            UPDATE "ingredients" SET "amount"="amount"+v_order_amount
            WHERE "id" = porderno;
            UPDATE "ingredient_orders" SET "status"=1 WHERE "id" = porderno;
        EXCEPTION
            WHEN no_data_found THEN
                RAISE e_no_such_row;
            WHEN OTHERS THEN
                RAISE;
        END;
    END "mark_ingredient_order_complete";

    PROCEDURE "mark_chocolate_order_complete"(
        porderno INTEGER
    ) IS
        v_i    NUMBER := 0;
        var_chocolate_id  "CHOCOLATE_ORDERS_ENTRY_VIEW"."chocolate_id"%TYPE;
        var_amount_available "CHOCOLATE_ORDERS_ENTRY_VIEW"."amount_available"%TYPE;
        var_amount_wanted "CHOCOLATE_ORDERS_ENTRY_VIEW"."amount_wanted"%TYPE;
        CURSOR order_content_cursor IS
            SELECT "chocolate_id", "amount_available", "amount_wanted"
            FROM "CHOCOLATE_ORDERS_ENTRY_VIEW"
            WHERE "order_id" = porderno;
    BEGIN
        IF porderno IS NULL THEN
            RAISE e_missing_argument;
        END IF;

        BEGIN
            SELECT "status" INTO v_i FROM "chocolate_orders" WHERE "id" = porderno;
            IF v_i = 1 THEN
                RAISE e_order_already_complete;
            END IF;

            SAVEPOINT "sv_pre_delete";

            UPDATE "chocolate_orders" SET "status"=1 WHERE "id" = porderno;

            OPEN order_content_cursor;
            LOOP
                FETCH order_content_cursor INTO
                    var_chocolate_id,
                    var_amount_available,
                    var_amount_wanted;
                EXIT WHEN order_content_cursor%notfound;
                IF var_amount_available < var_amount_wanted THEN
                    ROLLBACK TO "sv_pre_delete";
                    RAISE e_not_enough_to_fulfill;
                END IF;

                UPDATE "chocolates" SET "amount" = "amount" - var_amount_wanted
                                    WHERE "id" = var_chocolate_id;
            END LOOP;
        EXCEPTION
            WHEN no_data_found THEN
                ROLLBACK TO "sv_pre_delete";
                RAISE e_no_such_row;
            WHEN OTHERS THEN
                ROLLBACK TO "sv_pre_delete";
                RAISE;
        END;
    END "mark_chocolate_order_complete";

    PROCEDURE "delete_ingredient_from_chocolate"(
        pchocolateid INTEGER,
        pingredientid INTEGER
    ) IS
        v_i    NUMBER := 0;
    BEGIN
        IF pchocolateid IS NULL OR pingredientid IS NULL THEN
            RAISE e_missing_argument;
        END IF;

        BEGIN
            SELECT 1 INTO v_i FROM "ingredient_usage_in_chocolate"
            WHERE "chocolate_id" = pchocolateid AND "ingredient_id" = pingredientid;

            DELETE FROM "ingredient_usage_in_chocolate"
            WHERE "chocolate_id" = pchocolateid AND "ingredient_id" = pingredientid;
        EXCEPTION
            WHEN no_data_found THEN
                RAISE e_no_such_row;
            WHEN OTHERS THEN
                RAISE;
        END;
    END "delete_ingredient_from_chocolate";

    PROCEDURE "add_employee"(
        pemployeename VARCHAR2,
        pjobposition VARCHAR2
    ) IS
        v_salary REAL := 0;
    BEGIN
        IF pemployeename IS NULL OR pjobposition IS NULL THEN
            RAISE e_missing_argument;
        END IF;

        CASE pjobposition
            WHEN 'MANAGEMENT' THEN
                v_salary := 40;
            WHEN 'FACTORY_FLOOR' THEN
                v_salary := 25;
            ELSE
                RAISE e_bad_employee_position;
        END CASE;

        INSERT INTO "employees" ("employee_name", "salary") VALUES (pemployeename, v_salary);
    END "add_employee";

END "procedures_pkg";
/
