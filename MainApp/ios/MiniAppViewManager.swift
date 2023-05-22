import React

@objc(MiniAppViewManager)
class MiniAppViewManager: RCTViewManager {
  override static func requiresMainQueueSetup() -> Bool {
    return true
  }
  
  override func view() -> UIView! {
    return MiniAppView()
  }
  
  @objc(dispatch:)
  func dispatch(reactTag: NSNumber) {
    bridge.uiManager.prependUIBlock({_ , viewRegistry in
      let view = viewRegistry?[reactTag]
      if !(view is MiniAppView) {
        print("Invalid view returned from registry, expecting RCTVideo, got: ", String(describing: view))
      } else if let view = view as? MiniAppView {
        view.dispatch()
      }
    })
  }
}
