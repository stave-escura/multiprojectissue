
import com.typesafe.sbt.SbtNativePackager
import com.typesafe.sbt.packager.archetypes.JavaServerAppPackaging
import com.typesafe.sbt.packager.linux.LinuxPackageMapping
import com.typesafe.sbt.packager.linux.LinuxPlugin.autoImport._
import sbt.Keys._

enablePlugins(SbtNativePackager)

enablePlugins(JavaServerAppPackaging)

name := "multiprojectissue"

version := "1.0"

scalaVersion := "2.11.8"


lazy val commonSettings = Seq(
  organization := "someOrganisation",
  scalaVersion := "2.11.8",
  scalacOptions ++= Seq(
    "-deprecation",
    "-encoding", "UTF-8",
    "-feature",
    "-unchecked",
    "-Ywarn-dead-code"
  ),
  assemblyJarName in assembly := s"${name.value}.jar",
  test in assembly := {},
  assemblyMergeStrategy in assembly := {
    case "BUILD" => MergeStrategy.discard
    case other => MergeStrategy.defaultMergeStrategy(other)
  }
)

lazy val project1Settings = commonSettings ++ Seq(
  rpmVendor := "someOrganisation",
  packageDescription in Rpm := "Some description1",
  rpmLicense := Some("Copyright 2016 someOrganisation. All rights reserved."),
  rpmRequirements := Seq(
    "java-1.8.0-openjdk"
  ),
  version in Rpm := "1",
  rpmRelease := version.value,
  rpmDaemonLogFile := s"${name.value}.log",
  daemonUser in Linux := "someUserName",
  daemonGroup in Linux := (daemonUser in Linux).value,
  rpmPost := Some(
    """|chkconfig --add someService1
      |chkconfig someService1 on
    """.stripMargin),
  linuxPackageMappings ++= Seq()
)

lazy val project2Settings = commonSettings ++ Seq(
  rpmVendor := "someOrganisation",
  packageDescription in Rpm := "Some description2",
  rpmLicense := Some("Copyright 2016 someOrganisation. All rights reserved."),
  rpmRequirements := Seq(
    "java-1.8.0-openjdk"
  ),
  version in Rpm := "1",
  rpmRelease := version.value,
  rpmDaemonLogFile := s"${name.value}.log",
  daemonUser in Linux := "someUserName",
  daemonGroup in Linux := (daemonUser in Linux).value,
  rpmPost := Some(
    """|chkconfig --add someService2
      |chkconfig someService2 on
    """.stripMargin),
  linuxPackageMappings ++= Seq()
)

lazy val project1 = (project in file("components/service1")).settings(project1Settings: _*)

lazy val project2 = (project in file("components/service2")).settings(project2Settings: _*)

