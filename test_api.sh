#!/bin/bash

BASE_URL_CATALOG="http://localhost:8082/catalog"
BASE_URL_PAYMENT="http://localhost:8081/payment"

# Получение списка направлений
echo "📌 Получение списка направлений:"
curl -X GET "$BASE_URL_CATALOG/directions" -H "Content-Type: application/json"
echo -e "\n"

# Получение курсов по направлению
echo "📌 Получение курсов по направлению 'Test':"
direction=$(echo -n "Test" | jq -sRr @uri)
echo "Используемый URL: $BASE_URL_CATALOG/courses/$direction"
curl -X GET "$BASE_URL_CATALOG/courses/$direction" -H "Content-Type: application/json"
echo -e "\n"


# Получение информации о курсе
echo "📌 Получение информации о курсе (id=1):"
curl -X GET "$BASE_URL_CATALOG/course/1" -H "Content-Type: application/json"
echo -e "\n"

