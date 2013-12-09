name := "RandomRent"

version := "1.0-SNAPSHOT"

// "org.hibernate" % "hibernate-core" % "4.3.0.CR1",
// "org.hibernate" % "hibernate-entitymanager" % "4.3.0.CR1",
// "org.hibernate" % "hibernate-c3p0" % "4.3.0.Beta5",
// "org.hibernate.javax.persistence" % "hibernate-jpa-2.1-api" % "1.0.0.Draft-16",
// "org.apache.directory.api" % "apache-ldap-api" % "1.0.0-M14",
// "log4j" % "log4j" % "1.2.16",
 
libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  filters,
  "mysql" % "mysql-connector-java" % "5.1.18",
  "org.squeryl" %% "squeryl" % "0.9.5-6",
  "xml-apis" % "xml-apis" % "1.4.01",
  "com.typesafe.akka" %% "akka-actor" % "2.2.1",
  "com.typesafe.akka" %% "akka-testkit" % "2.2.1"
)     

play.Project.playScalaSettings
