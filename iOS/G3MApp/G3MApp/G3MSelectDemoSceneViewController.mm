//
//  G3MSelectDemoSceneViewController.m
//  G3MApp
//
//  Created by Diego Gomez Deck on 11/14/13.
//  Copyright (c) 2013 Igo Software SL. All rights reserved.
//

#import "G3MSelectDemoSceneViewController.h"

#include "G3MDemoModel.hpp"
#include "G3MDemoScene.hpp"

#import <G3MiOSSDK/NSString_CppAdditions.h>

@interface G3MSelectDemoSceneViewController ()

@end

@implementation G3MSelectDemoSceneViewController

@synthesize popoverController = _myPopoverController;

-(void)setDemoModel:(G3MDemoModel*) demoModel
{
  _demoModel = demoModel;
}

-(void)viewDidAppear:(BOOL)animated
{
  [super viewDidAppear:animated];

  CGSize currentSetSizeForPopover = self.contentSizeForViewInPopover;
  CGSize fakeMomentarySize = CGSizeMake(currentSetSizeForPopover.width - 1.0f, currentSetSizeForPopover.height - 1.0f);
  self.contentSizeForViewInPopover = fakeMomentarySize;
  self.contentSizeForViewInPopover = currentSetSizeForPopover;
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
  return 1;
}

- (NSInteger)tableView:(UITableView *)tableView
 numberOfRowsInSection:(NSInteger)section
{
  return _demoModel->getScenesCount();
}

- (UITableViewCell *)tableView:(UITableView *)tableView
         cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
  static NSString *CellIdentifier = @"Cell";
  UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier: CellIdentifier
                                                          forIndexPath: indexPath];

  const G3MDemoScene* scene = _demoModel->getScene( indexPath.row );
  const std::string sceneName = scene->getName();

  UIButton* button = (UIButton*) [cell viewWithTag: 100];
  [button setTitle: [NSString stringWithCppString: sceneName]
          forState: UIControlStateNormal];

  button.backgroundColor = _demoModel->isSelectedScene(scene) ? [UIColor lightGrayColor] : [UIColor clearColor];

  button.clipsToBounds = YES;
  button.layer.cornerRadius = 8;
  //button.layer.borderColor = [UIColor darkGrayColor].CGColor;
  //button.layer.borderWidth = 1;

  return cell;
}

- (IBAction)changeDemo:(UIButton *)sender {
  NSString* sceneName = [[sender titleLabel] text];
  //NSLog(@"Touched on \"%@\"", sceneName);

  _demoModel->selectScene( [sceneName toCppString] );

  [self.popoverController dismissPopoverAnimated:YES];
}

@end
