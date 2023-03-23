(ns json-app.core
  (:gen-class)
  (:require [clojure.data.json :as json]))





(def all-records (json/read-str (slurp "/home/xlyu/Downloads/operator-mappings.json")
                :key-fn keyword))

;1 list all countries

(defn getCountries 
  [data]
  (into []  (map #(% :country) data)) ) 

;2  List all unique Countries sorted alphabetically

(defn getUniqCountries 
  [data]
  (sort (distinct (getCountries data))))


;3. List all networks within a country {“Ireland” [“O2", “Vodafone”], “Spain” [“Orange”]}
(defn addNetwork [ls data]
  (let [bindingFirstJSON (first data)
        cournty (get :country bindingFirstJSON)
        network (get :name bindingFirstJSON)]
    reduce---
    )
  )

(defn getNetworks
  [data]
  (def ls (zipmap (getUniqCountries data) (repeat [])))
  ;; one go here
  ;create {:Afghanistan [] ....}
  (addNetwork ls data)
)

filter
reduce
->


; 4. For every Country that contains a Vodafone network, produce a map of country to a map value that contains a count of all networks for that country and the mapping info for its vodafone network, but remove tadig and lowercase the country name. e.g. {“ireland” {:count 10 :vodafone-mapping {:iso2 “IE”, :country “ireland” …..}}
; 5. Create a map of tadigs to their network names, for every single tadig
; 6. Create a map of country name to iso2

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (getUniqCountries all-records)
  )
