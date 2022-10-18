**AWS SES Template Creation:**
```bash
aws ses create-template --cli-input-json file:///Users/vrana/git/srot-backend/src/main/resources/email/ConfirmationEmailTemplate.json
aws ses send-templated-email --cli-input-json  file:///Users/vrana/git/srot-backend/src/main/resources/email/InvestorWelcomeEmailData.json
```
**Docker CMDs:**
```bash
docker build -t srot/srot-app:1.1-test . 
. env.sh
docker run -d -p 8080:8080 -e AWS_SECRET_ACCESS_KEY=$AWS_SECRET_ACCESS_KEY -e AWS_ACCESS_KEY_ID=$AWS_ACCESS_KEY_ID -e SPRING_APPLICATION_JSON=$SPRING_APPLICATION_JSON srot/srot-app:1.1-test
docker ps
docker logs -f <container_id>
docker kill <container_id>
```
**Build and Run:**
```bash
mvn dependency:tree
mvn clean install
java -jar /Users/vrana/git/srot-backend/target/Srot-0.0.1-SNAPSHOT.jar 
```
**Misc:**
```bash
mvn dependency:tree
```