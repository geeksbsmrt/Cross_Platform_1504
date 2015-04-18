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
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)viewDidAppear:(BOOL)animated {
	NSLog(@"Loading Objects");
	[self loadObjects];
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
	NSLog(@"In Query");
	PFQuery *query = [PFQuery queryWithClassName:self.parseClassName];
 
	// If no objects are loaded in memory, we look to the cache first to fill the table
	// and then subsequently do a query against the network.
	if (self.objects.count == 0) {
		query.cachePolicy = kPFCachePolicyCacheThenNetwork;
	}
	[query orderByDescending:@"Rating"];
 
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
		// Delete the object from Parse and reload the table view
		[[self.objects objectAtIndex:indexPath.row] deleteInBackgroundWithBlock:^(BOOL succeeded, NSError *error) {
			if (succeeded){
				[self loadObjects];
			}
		}];
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
