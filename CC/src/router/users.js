const { request } = require("express");
const express = require("express");
const router = express.Router();
const userController = require("../controller/userController");

router.post("/signup", userController.signup);
router.get("/login", userController.login);

module.exports = router;
