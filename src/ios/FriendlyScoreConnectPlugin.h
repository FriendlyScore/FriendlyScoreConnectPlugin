//
//  FriendlyScoreConnect.h
//  MyApp
//
//  Created by Lukasz Czechowicz on 25/05/2020.
//

#import <Foundation/Foundation.h>
#import <Cordova/CDVPlugin.h>

NS_ASSUME_NONNULL_BEGIN

@interface FriendlyScoreConnectPlugin : CDVPlugin
- (void)startFriendlyScoreConnect:(CDVInvokedUrlCommand*)command;
@end

NS_ASSUME_NONNULL_END
