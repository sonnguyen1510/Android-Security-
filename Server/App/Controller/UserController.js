const {User} = require('../Model/Model')

const UserController = {
    //add User
    addUser : async(req,res)=>{
        try{
            const newUser = new User(req.body)
            const saveUser = await newUser.save()
            console.log("Create user Successful")
            res.json(saveUser)
        }
        catch(error){
            console.log("Create user fail")
        }
    },
    //getAll user
    getAllUser : async(req,res) =>{
        try{
            const user = await User.find()
            res.json(user)
            console.log("Get all users success")
        }
        catch(error){
            console.log("Can't get user")
        }
    },
    //Get user by ID
    getUserbyID : async(req,res) =>{
        try {
            const user = await User.findById(req.params.UserID)
            res.json(user)
        } catch (error) {
            console.log("Can't get user")
        }
    },

    //Delete User by id
    
    DeleteUserbyID : async(req,res) =>{
        try {
            const UserDeleted = await User.DeleteUserbyID(req.params.UserID)
            res.json({
                Success : true ,
                DataDelete  : UserDeleted
            })
        } catch (error) {
            console.log("Delete user fail")
            req.status(404).send("Not Found")
        }
    }
}

module.exports = UserController