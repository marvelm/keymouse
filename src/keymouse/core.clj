(ns keymouse.core
  (:gen-class)
  (:import (org.jnativehook GlobalScreen
                            NativeHookException)
           (org.jnativehook.keyboard NativeKeyEvent
                                     NativeKeyListener)
           (org.jnativehook.mouse NativeMouseEvent
                                  NativeMouseInputListener)

           (java.awt Robot)))

(def robot (Robot.))

(def mousePos [0 0])
(defn moveMouse
  ([] (moveMouse mousePos))
  ([pos] (let [[x y] pos] (moveMouse x y)))
  ([x y] (let [x (if (< x 0) 0 x)
               y (if (< y 0) 0 y)]
           (do
             (def mousePos [x y])
             (.mouseMove robot x y)))))

(def keyListener
  (reify NativeKeyListener
    (nativeKeyPressed [this evt]
      (let [key (.getKeyCode evt)]
        (if (= key (NativeKeyEvent/VC_UP))
          (let [[x y] mousePos]
            (moveMouse x (- y 1))))))
    (nativeKeyReleased [this evt]
      (prn evt))
    (nativeKeyTyped [this evt]
      (prn evt))))

(def mouseListener
  (reify NativeMouseInputListener
    (nativeMouseClicked [this evt]
      (prn evt))
    (nativeMousePressed [this evt]
      (prn evt))
    (nativeMouseReleased [this evt]
      (prn evt))
    (nativeMouseMoved [this evt]
      (let [x (.getX evt)
            y (.getY evt)]
        (def mousePos [x y])))
    (nativeMouseDragged [this evt]
      (prn evt))))

(defn -main
  [& args]
  (moveMouse 0 0)
  (GlobalScreen/registerNativeHook)
  (GlobalScreen/addNativeKeyListener keyListener)
  (GlobalScreen/addNativeMouseMotionListener mouseListener)
  (println "Hello, World!"))