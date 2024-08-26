#### Steps:

- Download the json file of countries from [here](https://public.opendatasoft.com/api/explore/v2.1/catalog/datasets/world-administrative-boundaries/exports/json?lang=en).
- Change the name to `countries.json` & place it in the `/resources` directory
- Make sure you have `Docker` installed on your computer
- Simply run the `run.sh` file like `./run.sh`
- If you can not run bash file then just manually run the `docker-compose` to start the required services and then `mvn spring-boot:run`
- Since I'm running the project in `Ubuntu`, it may not be compatible with other operating systems, but it's not a big deal at all.
