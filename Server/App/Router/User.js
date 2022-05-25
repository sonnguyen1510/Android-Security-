var express = require('express')
const UserController = require('../Controller/UserController')

var router = express.Router()

//Add User
router.post("/Add_User",UserController.addUser)

//Add User
router.get("/get_all_user",UserController.getAllUser)

//get user by id
router.get("/get_user/:UserID",UserController.getUserbyID)

//delelet user by Id
router.delete("/delete/:ID",UserController.DeleteUserbyID)

//delete all user
router.delete("/delete_all",UserController.DeleteAllUser)

//
router.put("/update/:UserID",UserController.UpdateUserById)

/**router.get('/',function (req,res,next){
    console.log("Add get function")
}) */

module.exports = router