//
//  JuiceListViewController.m
//  testApp
//
//  Created by Adam Crawford on 4/17/15.
//  Copyright (c) 2015 Adam Crawford. All rights reserved.
//

#import "JuiceListViewController.h"

@interface JuiceListViewController ()

@end

@implementation JuiceListViewController

- (void)viewDidLoad {
    [super viewDidLoad];
	parseTimer = [NSTimer scheduledTimerWithTimeInterval:15 target:self selector:@selector(checkParse:) userInfo:nil repeats:YES];
}

-(void)checkParse:(NSTimer *) timer {
	BOOL connected = [(AppDelegate *) [[UIApplication sharedApplication] delegate] connected];
	if (connected) {
		NSLog(@"Checking Parse");
		[self loadObjects];
	}
}

-(void)viewDidAppear:(BOOL)animated {
	[self loadObjects];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (id)initWithCoder:(NSCoder *)aDecoder{
	self = [super initWithClassName:@"Juice"];
	self = [super initWithCoder:aDecoder];
	if (self) {
		// Custom the table
		
		// The className to query on
		self.parseClassName	= @"Juice";
		
		// Whether the built-in pull-to-refresh is enabled
		self.pullToRefreshEnabled = YES;
		
		// Whether the built-in pagination is enabled
		self.paginationEnabled = YES;
		
		// The number of objects to show per page
		self.objectsPerPage = 25;
	}
	return self;
}

- (PFQuery *)queryForTable {
	PFQuery *query = [PFQuery queryWithClassName:self.parseClassName];
	[query orderByDescending:@"Rating"];
	BOOL connected = [(AppDelegate *) [[UIApplication sharedApplication] delegate] connected];
	if (!connected) {
		[query fromPin];
	}
 
	return query;
}

- (UITableViewCell *)tableView:(UITableView *)tableView
		 cellForRowAtIndexPath:(NSIndexPath *)indexPath
						object:(PFObject *)object
{
	static NSString *cellIdentifier = @"JuiceCell";
 
	JuiceCell *cell = [tableView dequeueReusableCellWithIdentifier:cellIdentifier];
	if (!cell) {
		cell = [[JuiceCell alloc] initWithStyle:UITableViewCellStyleDefault
								 reuseIdentifier:cellIdentifier];
	}
	
	cell.juiceName.text = object[@"Flavor"];
	cell.juiceRating.text = [NSString stringWithFormat:@"%@", object[@"Rating"]];
 
	return cell;
}

- (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath {
	return YES;
}

- (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath {
	if (editingStyle == UITableViewCellEditingStyleDelete) {
		BOOL connected = [(AppDelegate *) [[UIApplication sharedApplication] delegate] connected];
		if (connected) {
			//Connected
			[[self.objects objectAtIndex:indexPath.row] deleteInBackgroundWithTarget:self selector:@selector(loadObjects)];
		} else {
			//Not Connected
			[[self.objects objectAtIndex:indexPath.row] unpinInBackgroundWithBlock:^(BOOL succeeded, NSError *error) {
				if (succeeded) {
					[self loadObjects];
				}
			}];
			[[self.objects objectAtIndex:indexPath.row] deleteEventually];
		}
	}
}

-(void)objectsDidLoad:(nullable NSError *)error {
	[super objectsDidLoad:error];
	if (error) {
		NSLog(@"error");
	} else {
		NSLog(@"Pinning");
		[PFObject pinAllInBackground:self.objects];
	}
}


#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
	NSIndexPath *path = [table indexPathForSelectedRow];
	JuiceDetailViewController *jdc = [segue destinationViewController];
	[jdc setSending:[self.objects objectAtIndex:path.row]];
}


@end
