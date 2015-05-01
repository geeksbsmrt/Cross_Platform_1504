//
//  AppDelegate.m
//  testApp
//
//  Created by Adam Crawford on 4/16/15.
//  Copyright (c) 2015 Adam Crawford. All rights reserved.
//

#import "AppDelegate.h"

@interface AppDelegate ()

@end

@implementation AppDelegate

@synthesize reachable;
@synthesize connected;

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
	// Override point for customization after application launch.
	
	[Parse enableLocalDatastore];
	[Parse setApplicationId:@"0AeWMQV8PXBKQunAXFzf7j0IaqF573OGed7mFN9y"
				  clientKey:@"JFicScwD8uAL4UhgHtMLuGo3YkkXwc3OO0Yt5YV4"];
	
	[[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(connectionChanged:) name:kReachabilityChangedNotification object:nil];
	reachable = [Reachability reachabilityForInternetConnection];
	
	NetworkStatus status = [reachable currentReachabilityStatus];
	if (status == NotReachable) {
		NSLog(@"Not connected");
		self.connected = NO;
	} else {
		NSLog(@"Connected");
		self.connected = YES;
		[self syncParse];
	}
	return YES;
}

- (void)applicationWillResignActive:(UIApplication *)application {
	// Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
	// Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
	NSLog(@"Stopping");
	[reachable stopNotifier];
}

- (void)applicationDidEnterBackground:(UIApplication *)application {
	// Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
	// If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
}

- (void)applicationWillEnterForeground:(UIApplication *)application {
	// Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
}

- (void)applicationDidBecomeActive:(UIApplication *)application {
	// Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
		NSLog(@"Starting");
	[reachable startNotifier];
}

- (void)applicationWillTerminate:(UIApplication *)application {
	// Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
}

-(void)connectionChanged:(NSNotification *)notice {
	NetworkStatus status = [reachable currentReachabilityStatus];
	if (status == NotReachable) {
		NSLog(@"Not connected");
		self.connected = NO;
	} else {
		NSLog(@"Connected");
		self.connected = YES;
		[self syncParse];
	}
}

-(void)syncParse {
	if (self.connected) {
		PFQuery *query = [PFQuery queryWithClassName:@"Juice"];
		[query fromPin];
		[query findObjectsInBackgroundWithBlock:^(NSArray *objects, NSError *error){
			if (!error) {
				[PFObject saveAllInBackground:objects block:^(BOOL succeeded, NSError *error){
					if (succeeded) {
						NSLog(@"Unpinning");
						[PFObject unpinAllInBackground:objects];
					}
				}];
			}
		}];
	}
}

@end
