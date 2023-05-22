#import <React/RCTViewManager.h>

@interface RCT_EXTERN_REMAP_MODULE(MiniApp, MiniAppViewManager, RCTViewManager)
RCT_EXPORT_VIEW_PROPERTY(source, NSDictionary)
RCT_EXPORT_VIEW_PROPERTY(onStartLoad, RCTDirectEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onEndLoad, RCTDirectEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onError, RCTDirectEventBlock)

RCT_EXTERN_METHOD(dispatch:(nonnull NSNumber *)reactTag)
@end
