const mongoose = require( 'mongoose')

const UserSchema  = new mongoose.Schema({
    Name :{
        type : String,
        required : true
    },

    Age:{
        type : String
    },
    Birthday : {
        type :String 
    },

    Gender : {
        type : Boolean
    },

    Email :{
        type : String
    },

    ImageEye : {
        type : String
    },

    ImageEyeCode:{
        type :[Number]
    }

})

const AdminSchema = new mongoose.Schema({
    Name :{
        type : String,
        required : true
    },

    User_name:{
        type : String,
        required : true
    },

    Password :{
        type : String,
        required :true
    },

    Age:{
        type : String
    },
    Birthday : {
        type :String 
    },

    Gender : {
        type : Boolean
    },
})

let User = mongoose.model("User",UserSchema),admin = mongoose.model("Admin",AdminSchema)
module.exports = {User,admin}
