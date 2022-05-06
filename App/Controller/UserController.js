const {User} = require('../Model/Model')

const UserController = {
    //add User
    addUser : async(req,res)=>{
        try{
            const newUser = new User(req.body)
            const saveUser = await newUser.save()
            console.log("Create user Successful")
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
    }
}

module.exports = UserController