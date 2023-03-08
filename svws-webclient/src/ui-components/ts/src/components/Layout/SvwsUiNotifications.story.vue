<template>
	<Story title="SVWS UI/Layout/Notifications">
		<svws-ui-app-layout>
			<template #secondaryMenu>
				<svws-ui-secondary-menu>
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
				</svws-ui-secondary-menu>
			</template>
			<template #main>
				<!--<div class="app-layout--main--placeholder">
					<i-ri-archive-line/>
				</div>-->
				<svws-ui-menu-header>
					<span>Title</span>
				</svws-ui-menu-header>
				<div class="px-9">
					Lorem ipsum dolor sit amet, consectetur adipisicing elit. Accusamus aliquam, atque
					earum est, explicabo in iste laudantium maiores molestiae necessitatibus quaerat
					quod ratione reprehenderit sapiente sit sunt temporibus tenetur voluptatum!
				</div>
				<svws-ui-notifications>
					<svws-ui-notification type="error">
						Error
					</svws-ui-notification>
					<svws-ui-notification>
						Info
					</svws-ui-notification>
					<svws-ui-notification type="error">
						Error
					</svws-ui-notification>
					<svws-ui-notification type="error">
						Error
					</svws-ui-notification>
				</svws-ui-notifications>
			</template>
		</svws-ui-app-layout>
	</Story>
</template>

<script setup lang="ts"></script>

<style>
html {
	font-size: 16px !important;
}
body {
	font-size: 0.875rem !important;
}
</style>