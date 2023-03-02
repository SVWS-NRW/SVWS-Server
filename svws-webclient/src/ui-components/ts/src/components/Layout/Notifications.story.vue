<template>
	<Story title="SVWS UI/Layout/Notifications">
		<AppLayout>
			<template #secondaryMenu>
				<SecondaryMenu>
					<template #headline>Sidebar</template>
					<template #content>
						<div class="px-6">
							<div>
								Lorem ipsum dolor sit amet, consectetur adipisicing elit. Accusamus aliquam, atque
								earum est, explicabo in iste laudantium maiores molestiae necessitatibus quaerat
								quod ratione reprehenderit sapiente sit sunt temporibus tenetur voluptatum!
							</div>
						</div>
					</template>
				</SecondaryMenu>
			</template>
			<template #main>
				<!--<div class="app-layout--main--placeholder">
					<i-ri-archive-line/>
				</div>-->
				<Header>
					<span>Title</span>
				</Header>
				<div class="px-9">
					Lorem ipsum dolor sit amet, consectetur adipisicing elit. Accusamus aliquam, atque
					earum est, explicabo in iste laudantium maiores molestiae necessitatibus quaerat
					quod ratione reprehenderit sapiente sit sunt temporibus tenetur voluptatum!
				</div>
				<Notifications>
					<Notification type="error">
						Error
					</Notification>
					<Notification>
						Info
					</Notification>
					<Notification type="error">
						Error
					</Notification>
					<Notification type="error">
						Error
					</Notification>
				</Notifications>
			</template>
		</AppLayout>
	</Story>
</template>

<style>
html {
	font-size: 16px !important;
}
body {
	font-size: 0.875rem !important;
}
</style>
