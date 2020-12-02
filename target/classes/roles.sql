INSERT INTO public.permission(
	id, description, name)
	VALUES (1, 'User has possibility to use CRUD operations on USERS.', 'A_CRUD_USERS');
INSERT INTO public.permission(
	id, description, name)
	VALUES (2, 'User has possibility to use CRUD operations on ROLES.', 'A_CRUD_ROLES');
INSERT INTO public.permission(
	id, description, name)
	VALUES (3, 'User has possibility to use CRUD operations on SERVICES and INDICATORS.', 'A_CRUD_SERVICES');
INSERT INTO public.permission(
    id, description, name)
    VALUES (4, 'User has possibility to use CRUD operations on BOOKINGS.', 'A_CRUD_BOOKINGS');
INSERT INTO public.permission(
    id, description, name)
    VALUES (5, 'User has possibility to use CRUD operations on ORDERS.', 'A_CRUD_ORDERS');
INSERT INTO public.permission(
    id, description, name)
    VALUES (6, 'User has possibility to use CRUD operations on INDICATORS.', 'A_CRUD_INDICATORS');
INSERT INTO public.permission(
    id, description, name)
    VALUES (7, 'Standard user with no special permissions.', 'AUTH_USER');

INSERT INTO public.role(
	id, description, name)
	VALUES (101, 'Manager with all permissions in order management', 'MANAGER');
INSERT INTO public.role(
	id, description, name)
	VALUES (102, 'Standard user with no special permissions', 'USER');

INSERT INTO public.role_permissions(
	permission_id, role_id)
	VALUES (1, 101);
INSERT INTO public.role_permissions(
	permission_id, role_id)
	VALUES (3, 101);
INSERT INTO public.role_permissions(
	permission_id, role_id)
	VALUES (4, 101);
INSERT INTO public.role_permissions(
	permission_id, role_id)
	VALUES (5, 101);
INSERT INTO public.role_permissions(
	permission_id, role_id)
	VALUES (6, 101);
INSERT INTO public.role_permissions(
	permission_id, role_id)
	VALUES (7, 101);

INSERT INTO public.role_permissions(
	permission_id, role_id)
	VALUES (7, 102);

INSERT INTO public.account(
    id, username, password, email, is_activated)
    VALUES (1000000, 'user1', '$2a$10$5P2URNSo/nu0wIwR7dirNO.M5xJr8/JXrKA7vfLn0QBvcQXNjAUSe', 'user1@test.com', true);

INSERT INTO public.account(
    id, username, password, email, is_activated)
    VALUES (1000001, 'user2', '$2a$10$5P2URNSo/nu0wIwR7dirNO.M5xJr8/JXrKA7vfLn0QBvcQXNjAUSe', 'user2@test.com', true);

INSERT INTO public.VERIFICATION_TOKEN(
    id, token, account_id)
    VALUES (1000000, 'token', 1000000);

INSERT INTO public.photosupp_user(
    id, name, surname, role_id, account_id)
    VALUES (1000000, 'Klient', 'Nazwisko', 102, 1000000);

INSERT INTO public.photosupp_user(
    id, name, surname, role_id, account_id)
    VALUES (1000001, 'Menedżer', 'Nazwisko', 101, 1000001);

INSERT INTO public.indicator(
    id, name, description, locale, base_amount, DOUBLE_PRICE)
    VALUES (1000000, 'Odległość od Częstochowy', 'Proszę podać liczbę kilometrów Państwa lokalizacji od Częstochowy', 'pl', 40, 50);

INSERT INTO public.indicator(
    id, name, description, locale, base_amount, DOUBLE_PRICE)
    VALUES (1000001, 'Distance from Czestochowa', 'Kindly provide number of kilometers to your localization from Czestochowa', 'en', 40, 50);

INSERT INTO public.indicator(
    id, name, description, locale, base_amount, DOUBLE_PRICE)
    VALUES (1000002, 'Szacowana liczba zdjęć', 'Proponujemy następującą ilość zdjęć', 'pl', 20, 200);

INSERT INTO public.indicator(
    id, name, description, locale, base_amount, DOUBLE_PRICE)
    VALUES (1000003, 'Predicted number of product', 'We propose the following number of photos', 'en', 20, 200);

INSERT INTO public.indicator(
    id, name, description, locale, base_amount, DOUBLE_PRICE)
    VALUES (1000004, 'Szacowana liczba filmów', 'Proponujemy następującą liczbę filmów', 'pl', 1, 150);

INSERT INTO public.indicator(
    id, name, description, locale, base_amount, DOUBLE_PRICE)
    VALUES (1000005, 'Predicted number of clips', 'We propose the following number of clips', 'en', 1, 150);

INSERT INTO public.indicator(
    id, name, description, locale, base_amount, DOUBLE_PRICE)
    VALUES (1000006, 'Szacowana liczba minut dla filmu', 'Dla filmu proponujemy taką liczbę minut', 'pl', 1, 200);

INSERT INTO public.indicator(
    id, name, description, locale, base_amount, DOUBLE_PRICE)
    VALUES (1000007, 'Predicted number of minutes for each clip', 'We propose the following number of minutes', 'en', 1, 200);

INSERT INTO public.indicator(
    id, name, description, locale, base_amount, DOUBLE_PRICE)
    VALUES (1000008, 'Szacowana liczba zdjęć ślubnych', 'Proponujemy następującą ilość zdjęć', 'pl', 100, 500);

INSERT INTO public.indicator(
    id, name, description, locale, base_amount, DOUBLE_PRICE)
    VALUES (1000009, 'Predicted number of product wedding photos', 'We propose the following number of photos', 'en', 100, 500);

INSERT INTO public.indicator(
    id, name, description, locale, base_amount, DOUBLE_PRICE)
    VALUES (1000010, 'Szacowana liczba minut dla filmu ślubnego', 'Dla filmu proponujemy taką liczbę minut', 'pl', 30, 400);

INSERT INTO public.indicator(
    id, name, description, locale, base_amount, DOUBLE_PRICE)
    VALUES (1000011, 'Predicted number of minutes for wedding movie', 'We propose the following number of minutes', 'en', 30, 400);

INSERT INTO public.service(
    id, name, description, locale, base_price)
    VALUES (1000000, 'Fotografia i film biznesowy', 'Znakomity sposób na promocję twojej działalności.', 'pl', 800);

INSERT INTO public.service(
    id, name, description, locale, base_price)
    VALUES (1000001, 'Business photography and film', 'The superb way to promote your company.', 'en', 800);

INSERT INTO public.SERVICE_INDICATORS(
    INDICATOR_ID, SERVICE_ID)
    VALUES (1000000,1000000);

INSERT INTO public.SERVICE_INDICATORS(
    INDICATOR_ID, SERVICE_ID)
    VALUES (1000001,1000001);

INSERT INTO public.SERVICE_INDICATORS(
    INDICATOR_ID, SERVICE_ID)
    VALUES (1000004,1000000);

INSERT INTO public.SERVICE_INDICATORS(
    INDICATOR_ID, SERVICE_ID)
    VALUES (1000005,1000001);

INSERT INTO public.SERVICE_INDICATORS(
    INDICATOR_ID, SERVICE_ID)
    VALUES (1000006,1000000);

INSERT INTO public.SERVICE_INDICATORS(
    INDICATOR_ID, SERVICE_ID)
    VALUES (1000007,1000001);

INSERT INTO public.SERVICE_INDICATORS(
    INDICATOR_ID, SERVICE_ID)
    VALUES (1000002,1000000);

INSERT INTO public.SERVICE_INDICATORS(
    INDICATOR_ID, SERVICE_ID)
    VALUES (1000003,1000001);

INSERT INTO public.service(
    id, name, description, locale, base_price)
    VALUES (1000002, 'Zdjęcia ślubne i film', 'Jeśli chcesz uwiecznić tą wyjątkową chwilę.', 'pl', 2500);

INSERT INTO public.service(
    id, name, description, locale, base_price)
    VALUES (1000003, 'Wedding movie and photos', 'If you wish to memorize this moment forever', 'en', 2500);

INSERT INTO public.SERVICE_INDICATORS(
    INDICATOR_ID, SERVICE_ID)
    VALUES (1000008,1000002);

INSERT INTO public.SERVICE_INDICATORS(
    INDICATOR_ID, SERVICE_ID)
    VALUES (1000009,1000003);

INSERT INTO public.SERVICE_INDICATORS(
    INDICATOR_ID, SERVICE_ID)
    VALUES (1000010,1000002);

INSERT INTO public.SERVICE_INDICATORS(
    INDICATOR_ID, SERVICE_ID)
    VALUES (1000011,1000003);

INSERT INTO public.SERVICE_INDICATORS(
    INDICATOR_ID, SERVICE_ID)
    VALUES (1000000,1000002);

INSERT INTO public.SERVICE_INDICATORS(
    INDICATOR_ID, SERVICE_ID)
    VALUES (1000001,1000003);

INSERT INTO public.service(
    id, name, description, locale, base_price)
    VALUES (1000004, 'Forografia i film produktowy', 'Kiedy potrzebujesz profesjonalnych zdjęć produktów dla Twojej strony internetowej', 'pl', 1500);

INSERT INTO public.service(
    id, name, description, locale, base_price)
    VALUES (1000005, 'Products photography and clips', 'When you need professional product photos and clips for your website', 'en', 1500);

INSERT INTO public.SERVICE_INDICATORS(
    INDICATOR_ID, SERVICE_ID)
    VALUES (1000000,1000004);

INSERT INTO public.SERVICE_INDICATORS(
    INDICATOR_ID, SERVICE_ID)
    VALUES (1000001,1000005);

INSERT INTO public.SERVICE_INDICATORS(
    INDICATOR_ID, SERVICE_ID)
    VALUES (1000002,1000004);

INSERT INTO public.SERVICE_INDICATORS(
    INDICATOR_ID, SERVICE_ID)
    VALUES (1000003,1000005);

INSERT INTO public.SERVICE_INDICATORS(
    INDICATOR_ID, SERVICE_ID)
    VALUES (1000004,1000004);

INSERT INTO public.SERVICE_INDICATORS(
    INDICATOR_ID, SERVICE_ID)
    VALUES (1000005,1000005);

INSERT INTO public.SERVICE_INDICATORS(
    INDICATOR_ID, SERVICE_ID)
    VALUES (1000006,1000004);

INSERT INTO public.SERVICE_INDICATORS(
    INDICATOR_ID, SERVICE_ID)
    VALUES (1000007,1000005);

INSERT INTO public.address(
    id, CITY, STREET, BUILDING_NUMBER, APARTMENT_NUMBER, POSTAL_CODE)
    VALUES (1000000, 'Wroclaw', 'Wróblewskiego', '27', null, '51-627');

INSERT INTO public.booking(
    id, NAME, DESCRIPTION, START_DATE, END_DATE, FINAL_PRICE, MODIFICATION_DATE, IS_CONFIRMED, ADDRESS_ID, USER_ID, SERVICE_ID)
    VALUES (1000000, 'Film dla TestCompany', 'Film produktowy z dojazdem', '2020-04-11', '2020-04-12', 1400, '2020-04-11', false, 1000000, 1000000, 1000004);

INSERT INTO public.price_indicator(
    INDICATOR_ID, BOOKING_ID, PRICE, AMOUNT)
    VALUES (1000000, 1000000, 400, 10);

INSERT INTO public.photosupp_order(
    ORDER_NUMBER, USER_ID, COORDINATOR_ID, STATUS, BOOKING_ID, CREATED_AT, PRICE)
    VALUES ('INVIU_10001', 1000000, 1000001, 'NEW', null, '2020-09-11', 1000);

INSERT INTO public.PHOTOSUPP_COMMENT(
    id, CONTENT, ORDER_ID, USER_ID, CREATED_AT)
    VALUES (1000000, 'Perfect, thanks!', 'INVIU_10001', 1000001, '2020-09-11');

INSERT INTO public.media_content(
    id, TYPE, URL, ORDER_ID)
    VALUES (1000000, 'IMAGE', 'https://sample.com/jpg1', 'INVIU_10001');