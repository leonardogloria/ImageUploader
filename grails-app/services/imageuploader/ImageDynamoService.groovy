package imageuploader

import grails.plugin.awssdk.dynamodb.AbstractDBService
import grails.transaction.Transactional
import util.Image

@Transactional
class ImageDynamoService extends AbstractDBService<Image> {
    ImageDynamoService(){
        super(Image)
    }
}
