# Three TyrZ Starter

An application template for building functional programs that generate 2d and 3d graphics on the web using Three.js, Scala.js, Tyrian, and ZIO

## Setup Instructions

I recommend keeping two terminals open: one for npm and one for sbt.

* Install Prerequisites: Java, SBT, Node

* Install application's node dependencies:
```sh
npm install
```

* Run the tests:
```sh
sbt test
```

* Build the Scala code:
```sh
sbt fastLinkJS
```

* Package the javascript and start a build server:
```sh
npm start
```

Now navigate to [http://localhost:1234/](http://localhost:1234/) to see your site running.
