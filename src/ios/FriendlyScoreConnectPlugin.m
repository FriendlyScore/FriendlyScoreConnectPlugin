//
//  FriendlyScoreConnect.m
//  MyApp
//
//  Created by Lukasz Czechowicz on 25/05/2020.
//

#import "FriendlyScoreConnectPlugin.h"
#import <Cordova/CDVPlugin.h>
@import FriendlyScoreSDK;

@implementation FriendlyScoreConnectPlugin

NSString *clientId = @"";
NSString *environment = @"production";

- (void)startFriendlyScoreConnect:(CDVInvokedUrlCommand*)command
{
    NSString* userRef = [command.arguments objectAtIndex:0];
    [FS connectWithClientId:clientId userReference:userRef environment:environment];
    
    FS.errorHandler = ^( NSString* error) {
        CDVPluginResult* pluginResult = nil;
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:error];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    };
    FS.eventHandler = ^( NSString* event) {
        CDVPluginResult* pluginResult = nil;
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:event];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    };
    
    
}
@end
