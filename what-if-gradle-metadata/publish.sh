#!/usr/bin/env sh
cd libraries
./gradlew publish -PjacksonVersion=2.9.8 -PandroidxVersion=1.0.0 -PhibernateVersion=6.0.0.Alpha1
./gradlew publish -PjacksonVersion=2.9.9 -PandroidxVersion=1.0.1 -PhibernateVersion=6.0.0.Alpha2
cd ..