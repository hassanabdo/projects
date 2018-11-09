# fetch basic image
FROM maven:3.3.9-jdk-8

RUN apt-get update && apt-get install -y r-base

RUN R -e "install.packages('MASS', repos='http://cran.us.r-project.org/')"
RUN R -e "install.packages('rjson', repos='http://cran.us.r-project.org/')"
RUN R -e "install.packages('Matrix', repos='http://cran.us.r-project.org/')"
RUN R -e "install.packages('rNMF', repos='http://cran.us.r-project.org/')"
RUN R -e "install.packages('Rserve', repos='http://cran.us.r-project.org/')"

# application placed into /opt/app
RUN mkdir -p /persona
WORKDIR /persona

# selectively add the POM file and
# install dependencies
COPY pom.xml /persona
RUN mvn install

# rest of the project
COPY src /persona
RUN mvn package

# local application port
EXPOSE 8080

COPY run.sh RserverScript.R persona_names.json peoples.json NMFCreation_new.r map.json countries.json /persona/

RUN chmod +x run.sh

RUN ls -a

CMD /persona/run.sh