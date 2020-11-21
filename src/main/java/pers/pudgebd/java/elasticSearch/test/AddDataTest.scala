package pers.pudgebd.java.elasticSearch.test

import pers.pudgebd.java.utils.HttpUtils
import pers.pudgebd.java.utils.constants.{EsCnts, Key}

import scala.collection.mutable.ArrayBuffer

/**
  * Created by Administrator on 2017/2/11.
  */
object AddDataTest {

    case class Employee(id: String, doc: String)

    def add1() = {
        val bodys = ArrayBuffer.empty[Employee]
        bodys += Employee("1", "{\"first_name\":\"John\",\"last_name\":\"Smith\",\"age\":25,\"about\":\"I love to go rock climbing\",\"interests\":[\"sports\",\"music\"]}")
        bodys += Employee("2", "{\"first_name\":\"Jane\",\"last_name\":\"Smith\",\"age\":32,\"about\":\"I like to collect rock albums\",\"interests\":[\"music\"]}")
        bodys += Employee("3", "{\"first_name\":\"Douglas\",\"last_name\":\"Fir\",\"age\":35,\"about\":\"I like to build cabinets\",\"interests\":[\"forestry\"]}")

        for (emp <- bodys) {
//            val resp = HttpClientUtils.requestPut(EsCnts.MEGACORP_BASIC_URL + emp.id, StringUtils.getUtf8Bytes(emp.doc), null)
//            println(resp)
        }
    }

    def add2() = {
        val idxUrl = s"http://${EsCnts.HOST}:9200/my_store"
        try {
//            HttpClientUtils.requestDel(idxUrl, null)
        } catch {
            case e: Exception => e.printStackTrace()
        }

        val putBody = """                {
                        |                    "mappings" : {
                        |                        "products" : {
                        |                        "properties" : {
                        |                        "productID" : {
                        |                        "type" : "string",
                        |                        "index" : "not_analyzed"
                        |                    }
                        |                    }
                        |                    }
                        |                    }
                        |
                        |                }""".stripMargin
        println(putBody)
//        HttpClientUtils.requestPut(idxUrl, putBody, null)

        println()
        val body2 = """{ "index": { "_id": 1 }}
                      |{ "price" : 10, "productID" : "XHDK-A-1293-#fJ3" }
                      |{ "index": { "_id": 2 }}
                      |{ "price" : 20, "productID" : "KDKE-B-9947-#kL5" }
                      |{ "index": { "_id": 3 }}
                      |{ "price" : 30, "productID" : "JODL-X-1937-#pV7" }
                      |{ "index": { "_id": 4 }}
                      |{ "price" : 30, "productID" : "QQPX-R-3956-#aD8" }""".stripMargin
//        val resp = HttpClientUtils.requestPost(EsCnts.MY_STORE_BASIC_URL + Key._BULK, body2, null)
//        println(resp)
    }

    def main(args: Array[String]): Unit = {
        add1()
        add2()

    }

}














