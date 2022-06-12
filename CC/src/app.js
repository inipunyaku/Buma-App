const express = require("express");
const articleRouter = require("./router/article");
const usersRouter = require("./router/users");
const app = express();
const cors = require("cors");
const bodyParser = require("body-parser");

app.disable("x-powered-by");
app.use(cors());
app.use(express.json());
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));

app.use(articleRouter);
app.use(usersRouter);

app.listen(process.env.PORT || 3000, () => {
  console.log("Example app listening on http://localhost:3000");
});

//Run app, then load http://localhost:port in a browser to see the output.
