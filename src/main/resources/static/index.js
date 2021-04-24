Vue.filter('format', function (value) {
  return new Intl.NumberFormat('en-IN', { maximumFractionDigits: 2 }).format(value);
})

var app = new Vue({
    el: '#app',
    data: {
        feature: "products_without_fulltext", // default search feature
        query: "cutie", // default search term
        searchResponse: null,
        filters: [],
        from : 0,
        age_from: null,
        age_to: null,
        features : {
          products_without_fulltext : {
            url : "products_without_fulltext"
          },
          products_only : {
            url : "products_only"
          },
          products_with_filtered_aggs : {
            url : "products_with_filtered_aggs"
          },
          products_with_filtered_aggs_with_boost : {
             url : "products_with_filtered_aggs_with_boost"
          }
        }
    },
    watch: {
      filters: function(newFilters, oldFilters) {
        this.search();
      },
      feature: function(newFeature, oldFeature) {
        // reset filters
        this.filters.splice(0, this.filters.length);
        this.searchResponse = null
      },
      from: function(newFrom, oldFrom) {
        this.search();
      }
    },
    methods: {
      add_age_filter : function() {
        console.log("GOT AGE", this.age_from, " TO ", this.age_to)
        index = this.filters.findIndex( function(e) { return e.key == "Age" } )
        lower = this.age_from !== null ? this.age_from : ""
        upper = this.age_to !== null ? this.age_to : ""
        filter = { key: 'Age', value: lower + "-" + upper, type: 'range', from: lower, to: upper}
        if (index === undefined) {
            this.filters.push(filter)
        } else {
            this.filters.splice(index, 1, filter)
        }
      },
      search : function() {
        url = this.features[this.feature].url
        query = { query: this.query, from: this.from }
        if (this.filters !== null) {
          query.filters = this.filters
        }
        console.log("URL", url, " and query ", JSON.stringify(query))
        axios
          .post("http://localhost:8080/search/" + url, query)
          .then(response => {
            console.log("RESPONSE ", response.data)
            this.searchResponse = response.data
          })
      }
    }
});
