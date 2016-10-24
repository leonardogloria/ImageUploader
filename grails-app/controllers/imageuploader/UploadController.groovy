package imageuploader

import groovy.json.JsonBuilder
import util.Image

class UploadController {
    def amazonS3Service
    def amazonSQSService
    def createTable(){
        def ctx = grailsApplication.mainContext

        ctx.imageDynamoService.createTable()

    }
    def doUpload() {
        def ctx = grailsApplication.mainContext
        def file = request.getFile('myFile')
        int fileExtIndex = file.originalFilename.lastIndexOf(".")
        file.originalFilename
        String extName = file.originalFilename.substring(fileExtIndex)
        String id = UUID.randomUUID().toString()

        if(!extName.equalsIgnoreCase(".jpg") && !extName.equalsIgnoreCase(".png")){

            def errorMSG ="O arquivo deve possuir uma  das extensões: '.jpg', '.png',"

        }


        amazonS3Service.storeMultipartFile('lgloriaworkshopaws', "${id + extName}", file)

        def hashKey = 123456789L
        def rangeKey = '6733308f-5f64-48d2-824d-665290664ae0'

        def image= new Image()
        image.fileName = "${id + extName}"
        image.accountId = hashKey
        image.creationDate = new Date()
        image.status = "pending"
        ctx.imageDynamoService.save(image)

        def json = new JsonBuilder()
        def root = json{
            "uuid" id
            "filename" "${id + extName}"
            "type" params.type


        }


        amazonSQSService.sendMessage("images", json.toString())


    }
}
