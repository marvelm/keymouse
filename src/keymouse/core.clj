(ns keymouse.core
  (:gen-class)
  (:import (org.jnativehook GlobalScreen
                            NativeHookException)
           (org.jnativehook.keyboard NativeKeyEvent
                                     NativeKeyListener)
           (org.jnativehook.mouse NativeMouseEvent
                                  NativeMouseInputListener)

           (java.awt Robot)
           (java.awt.event InputEvent)))

(def robot (Robot.))

(def enabled true)

(def mousePos [0 0])
(defn moveMouse
  ([] (let [[x y] mousePos] (moveMouse x y)))
  ([f] (let [[x y] mousePos
             [x' y'] (f x y)]
         (moveMouse x' y')))
  ([x y] (let [x (if (< x 0) 0 x)
               y (if (< y 0) 0 y)]
           (if enabled
             (do
               (def mousePos [x y])
               (.mouseMove robot x y))))))

(defn leftClick
  ([] (if enabled
        (do
          (moveMouse)
          (.mousePress robot InputEvent/BUTTON1_MASK)
          (.mouseRelease robot InputEvent/BUTTON1_MASK)))))
(defn rightClick
  ([] (if enabled
        (do
          (moveMouse)
          (.mousePress robot InputEvent/BUTTON3_MASK)
          (.mouseRelease robot InputEvent/BUTTON3_MASK)))))

(def keyListener
  (reify NativeKeyListener
    (nativeKeyPressed [this evt]
      (let [key (.getKeyCode evt)]
        (condp = key
          NativeKeyEvent/VC_BACK_SLASH
          (def enabled (not enabled))

          NativeKeyEvent/VC_W
          (moveMouse (fn [x y] [x (- y 5)]))

          NativeKeyEvent/VC_S
          (moveMouse (fn [x y] [x (+ y 5)]))

          NativeKeyEvent/VC_D
          (moveMouse (fn [x y] [(+ x 5) y]))

          NativeKeyEvent/VC_A
          (moveMouse (fn [x y] [(- x 5) y]))

          NativeKeyEvent/VC_SEMICOLON
          (leftClick)

          NativeKeyEvent/VC_QUOTE
          (rightClick)

          (println "unmatched" key))))
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
