FROM 326141087440.dkr.ecr.ap-south-1.amazonaws.com/maven:3.8.1-openjdk-16-slim@sha256:6298789f4fd355c9f00484117dcda3e5e27d863cf0c376f51770b34ae2cd9d1d AS build
RUN mkdir /project
COPY . /project
WORKDIR /project
RUN mvn clean package -DskipTests


FROM 326141087440.dkr.ecr.ap-south-1.amazonaws.com/adoptopenjdk/openjdk16:jre-16.0.1_9-alpine@sha256:5e4b3b9de1c38a4797d9b0f78171bb551b26553a617611f57ce6b1cadd4e97bd
RUN mkdir /app
RUN addgroup --system javauser && adduser -S -s /bin/false -G javauser javauser
COPY --from=build /project/target/Srot-0.0.1-SNAPSHOT.jar /app/srot-app.jar
WORKDIR /app
RUN chown -R javauser:javauser /app
USER javauser
CMD "java" "-jar" "srot-app.jar"