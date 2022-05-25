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
            console.log("Get User by ID success")
        } catch (error) {
            console.log("Can't get user")
        }
    },

    //Delete User by id
    
    DeleteUserbyID : async(req,res) =>{
        try {
            await User.findByIdAndDelete(req.params.ID)
            res.json({
                Success : true
                
            })
        } catch (error) {
            console.log("Delete user fail")
            
        }
    },

    DeleteAllUser : async(req,res) =>{
        try {
            await User.deleteMany()
            res.json({
                Success : true
                
            })
        } catch (error) {
            console.log("Delete user fail")
            
        }
    },

    UpdateUserById : async(req,res)=>{
        try {
            await User.findByIdAndUpdate(req.params.UserID,req.body,{
                Success : true
            })
            res.json({
                Success:true
            })
        } catch (error) {
            console.log("Update user fail")
        }
    }

}

module.exports = UserController