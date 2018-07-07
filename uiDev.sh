#launch ui with local proxy to play framework backend serving api
cd ui
polymer serve --proxy-target http://localhost:9000/api --proxy-path api
