(ns json-app.core
  (:gen-class)
  (:require [clojure.data.json :as json])
  (:require [clojure.string]))

(comment
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.clojure/data.json "2.4.0"]]
  )


(def operator-mappings 
  (json/read-str (slurp "/home/xlyu/Downloads/operator-mappings.json")
                :key-fn keyword))


(defn get-all-countries
  "1 list all countries"
  [operator-mappings]
  (into
   []
   (map :country operator-mappings)) )



(defn get-uniq-countries
  "2  List all unique Countries sorted alphabetically"
  [operator-mappings]
  (sort
   (distinct
    (get-all-countries operator-mappings))))


(defn country-operators 
  "3. List all networks within a country"
  [operator-mappings]
  (reduce 
   (fn 
     [country-operators operator-mapping]
     (let
      [country (:country operator-mapping) ;get "Agentina" 
       operators (get country-operators country);get {} "Agentina"=>nil 
       operators (if operators operators []);if operator is nil, return [], otherwise return operator
       ]
     (assoc
      country-operators
      (:country operator-mapping) (conj operators (:name operator-mapping))))) 
   {}
   operator-mappings)
)


;{“ireland” {:count 10 :vodafone-mapping {:iso2 “IE”, :country “ireland” …..}}
(defn country-count-mapping
  "4. For every Country that contains a Vodafone network, produce a map of country to a map value that contains a count of all networks for that country and the mapping info for its vodafone network, but remove tadig and lowercase the country name
   step 1 : all countries mapping, no matter with or without vodafone"
  [operator-mappings]
  (reduce
   (fn
     [country-vodafone operator-mapping]
     (let
      [country (:country operator-mapping)
       count-vodafone-mapping (get country-vodafone country) 
       count-vodafone-mapping (if count-vodafone-mapping
                                (update count-vodafone-mapping :count inc)
                                (assoc {} :count 1 :vodafone-mapping operator-mapping))] 
       (assoc country-vodafone country count-vodafone-mapping)))
   {} operator-mappings))


(defn countries-with-vodafone
  "a vector contain all the countris' name that have vodafone network"
  [operator-mappings]
  (reduce
   (fn [countries country-operator]
     (let
      [operators (clojure.string/join (second country-operator))
       operators (if (re-find #"Vodafone" operators )
                   (first country-operator))]
       (if operators
         (conj countries operators)
         countries)
     ))
   [] (country-operators operator-mappings)
  ))


(defn filter-country-count-mapping
  "filter the result"
  [operator-mappings]
  (let
   [country-count-mappings (country-count-mapping operator-mappings)
    countries-with-vodafones (countries-with-vodafone operator-mappings)]
    (reduce
     (fn [filter-country-count country-count-mapping]
       (if (contains? (set countries-with-vodafones)  (first country-count-mapping))
         (conj filter-country-count country-count-mapping)
         filter-country-count))
     {} country-count-mappings))
    )


(defn tidigs-names
  "5. Create a map of tadigs to their network names, for every single tadig"
  [operator-mappings]
  (reduce 
   (fn [tadig-network operator-mapping] 
     (let
      [tadigs-coll (:tadig operator-mapping)]
       (reduce
        (fn
          [tadig-network tidig]
          (assoc tadig-network tidig (:name operator-mapping)))
        tadig-network tadigs-coll)))
   {} operator-mappings))


(defn map-country-iso2
  "6. Create a map of country name to iso2"
  [operator-mappings]
  (reduce ; get each set from operator-mappings
   (fn
     [country-iso2 operator-mapping] ; take a accumulator and a set from operator-mappings
     (let
      [country-name (:country operator-mapping) ;full country name 
       iso2 (:iso2 operator-mapping) ;iso2 of the country name 
       iso2-key (keyword iso2) ;set iso2 value as keyword of new map
       ]
       (assoc
        country-iso2
        iso2-key country-name))) ;assoc with iso2 as key, full country name as value
   {} operator-mappings))


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (countries-with-vodafone operator-mappings)
  )

(-main)

(ns-publics 'clojure.core)