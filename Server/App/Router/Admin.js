var express = require('express')
const AdminController = require('../Controller/AdminController')

var router = express.Router()

//add an admin
router.post("/Add_admin",AdminController.addAdmin)

//check admin
router.get("/Check_admin/:User_name/:Password",AdminController.CheckAdmin)

//get admin by id

router.get("/Get_admin/:AdminID",AdminController.GetAdminByID)

module.exports = router