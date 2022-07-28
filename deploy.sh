gradle clean

gradle buildDependents

cd authorization-service

docker build  -t testcase/authorization-service-1.0 .

cd ..

cd delivery-service

docker build -t testcase/delivery-service-1.0 .

cd ..

cd courier-service

docker build -t testcase/courier-service-1.0 .

cd ..

cd user-service

docker build -t testcase/user-service-1.0 .

cd ..



cd api-gateway

docker build  -t testcase/api-gateway-1.0 .

cd ..

docker compose up -d


read  -n 1 -p "Input Selection:" mainmenuinput