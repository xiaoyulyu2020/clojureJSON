(ns json-app.core
  (:gen-class)
  (:require [clojure.data.json :as json]))





(def operator-mappings (json/read-str (slurp "/home/xlyu/Downloads/operator-mappings.json")
                :key-fn keyword))

;1 list all countries

(defn get-all-countries 
  [operator-mappings]
  (into
   []
   (map :country operator-mappings)) )


;2  List all unique Countries sorted alphabetically

(defn get-uniq-countries
  [operator-mappings]
  (sort
   (distinct
    (get-all-countries operator-mappings))))


; ;3. List all networks within a country {“Ireland” [“O2", “Vodafone”], “Spain” [“Orange”]}

(defn country-operators 
  [operator-mappings]
  (reduce 
   (fn 
     [country-operators operator-mapping]
     (let [country (:country operator-mapping) ;get "Agentina"
          operators (get country-operators country);get {} "Agentina"=>nil
          operators (if operators operators []);if operator is nil, return [], otherwise return operator
          ]
     (assoc country-operators (:country operator-mapping) (conj operators (:name operator-mapping))))) 
   {}
   operator-mappings)
)

; {
;     "tadig": [
;       "ARGTM",
;       "ARG01"
;     ],
;     "name": "Telef\u00f3nica M\u00f3viles Argentina S.A.",
;     "iso3": "ARG",
;     "e164": [
;       "5407"
;     ],
;     "e212": [
;       "72207",
;       "722010"
;     ],
;     "realm": [
;       "epc.mnc007.mcc722.3gppnetwork.org"
;     ],
;     "country": "Argentina",
;     "iso2": "AR"
;   },
;   {
;     "tadig": [
;       "ROM05"
;     ],
;     "name": "S.C. RCS & RDS S.A.",
;     "iso3": "ROU",
;     "e164": [
;       "40770"
;     ],
;     "e212": [
;       "22605"
;     ],
;     "realm": [
;       "epc.mnc005.mcc226.3gppnetwork.org"
;     ],
;     "country": "Romania",
;     "iso2": "RO"
;   },

; 4. For every Country that contains a Vodafone network, produce a map of country to a map value that contains a count of all networks for that country and the mapping info for its vodafone network, but remove tadig and lowercase the country name. e.g. {“ireland” {:count 10 :vodafone-mapping {:iso2 “IE”, :country “ireland” …..}}


; 5. Create a map of tadigs to their network names, for every single tadig
; 6. Create a map of country name to iso2

(defn -main
  "I don't do a whole lot ... yet."
  [& args] 
  (country-operators operator-mappings)
  )

(-main)
