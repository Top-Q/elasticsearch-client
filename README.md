
Elasticsearch Java Client
=========================

A simple Elasticseach client based on the 5.x HTTP API. This is by no means a full blown client and it support only a small sub set of the Elasticseach operations.
Furethemore, when the Elasticteam will release a full Java client, this client will probably become redundent. 
Nevertheless, the client is very simple for use, supports the basic operations and is designed to easily be extended.

The supported operations are:

* Index creation and deletion
* Document creation and deletion
* Search by term and by query
* Aggregations with query

## Getting started

**prerequisites**

* Java 8

**Maven**

```Xml

    <repositories>
        <repository>
            <id>topq</id>
            <url>http://maven.top-q.co.il/content/groups/public</url>
        </repository>
    </repositories>
    .
    .
    .
    <dependencies>
    .
    .
        <dependency>
            <groupId>il.co.topq.elastic</groupId>
            <artifactId>elasticseach-client</artifactId>
            <version>1.0.00</version>
        </dependency>

    .
    .

    </dependencies>

```


## Usage Examples

**Index Operations**

*Create and delete*

```Java

String settings = "{\"settings\": { \"index\": { \"number_of_shards\": 3, \"number_of_replicas\": 1  }}}";
String index = "reddit";

try (ESClient client = new ESClient("localhost", 9200)) {
    client
       .index(index)
       .create(settings);

    client
       .index(index)
       .delete();
}       

```

**Document Operations**

*Add*

```Java
String index = "reddit";
String doc = "post";

Post post0 = new Post();
post0.setId(1212);
post0.setOp("Itai");
post0.setPoints(100);
post0.setSubreddit("all");

try (ESClient client = new ESClient("localhost", 9200)) {
    client
        .index(index)
        .document(doc)
        .add()
        .single("100", post0);
}

```

*Delete*

```Java
String index = "reddit";
String doc = "post";

try (ESClient client = new ESClient("localhost", 9200)) {
    Map<String, Object> response = client
        .index(index)
        .document(doc)
        .delete()
        .single("100");
    
    Assert.assertEquals("deleted", response.get("result").toString());
}

```


**Search Operations**

*Search by term*

```Java
String index = "reddit";
String doc = "post";

try (ESClient client = new ESClient("localhost", 9200)) {
    List<Post> posts = client
        .index(index)
        .document(doc)
        .search()
        .byTerm("id", "1212")
        .asClass(Post.class);
}

```

*Search by string query*

```Java
String index = "reddit";
String doc = "post";

try (ESClient client = new ESClient("localhost", 9200)) {
    List<Post> posts = client
        .index(index)
        .document(doc)
        .search()
        .byQuery("id:1212")
        .asClass(Post.class);
}

```

**Aggregation operations**

*Min aggregation*

```Java
String index = "reddit";
String doc = "post";

try (ESClient client = new ESClient("localhost", 9200)) {
    Double  = client
        .index(index)
        .document(doc)
        .aggs()
        .min("id");        
}
```

*Max aggregation*

```Java
String index = "reddit";
String doc = "post";

try (ESClient client = new ESClient("localhost", 9200)) {
    Double  = client
        .index(index)
        .document(doc)
        .aggs()
        .max("id");        
}
```
*Max aggregation with string query*

```Java
String index = "reddit";
String doc = "post";

try (ESClient client = new ESClient("localhost", 9200)) {
    Double  = client
        .index(index)
        .document(doc)
        .aggs()
        .max("id","foo:bar");        
}
```

