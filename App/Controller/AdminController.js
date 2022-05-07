const{admin} = require("../Model/Model")

const AdminController = {
    addAdmin :async(req,res) =>{
        try {
            const newAdmin = new admin(req.body)
            const saveAdmin = await newAdmin.save()
            res.json(saveAdmin)
            console.log("Create admin success")
        } catch (error) {
            console.log("Create admin fail")
        }
    },

    CheckAdmin : async(req,res) =>{
        try {
            const FindAdmin = await admin.findOne({
                User_name : req.params.User_name,
                Password : req.params.Password
            })
            res.json(FindAdmin)
            console.log("Find succcess")
        } catch (error) {
           console.log("No admin") 
        }
    },

    GetAdminByID : async(req,res) =>{
        try {
            const findAdmin = await admin.findById(req.params.AdminID)
            res.json(findAdmin)
        } catch (error) {
            console.log("No admin") 
        }
    }
}

module.exports =AdminController