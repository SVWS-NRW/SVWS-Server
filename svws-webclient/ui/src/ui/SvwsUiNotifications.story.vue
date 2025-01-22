<template>
	<Story title="Notifications" id="svws-ui-notifications" group="app" :layout="{type: 'grid', width: '45%'}" icon="ri:notification-badge-line">
		<Variant title="Default/Info" id="default">
			<div class="p-4 pb-8">
				<svws-ui-notification>
					<p>Info</p>
				</svws-ui-notification>
			</div>
		</Variant>
		<Variant title="Default/Info" id="info">
			<div class="p-4 pb-8">
				<svws-ui-notification>
					<template #header>
						Info
					</template>
					<p>Lorem ipsum dolor sit amet consectetur adipisicing elit. Expedita, eos consequatur inventore, voluptate exercitationem labore cupiditate distinctio nesciunt quisquam mollitia perspiciatis laboriosam sint eius itaque vitae accusamus dolorem molestias adipisci!</p>
				</svws-ui-notification>
			</div>
		</Variant>
		<Variant title="Success" id="Success">
			<div class="p-4 pb-8">
				<svws-ui-notification type="success">
					<template #header>
						Success
					</template>
					<p>Das hat gut geklappt!</p>
				</svws-ui-notification>
			</div>
		</Variant>
		<Variant title="Warning" id="Warning">
			<div class="p-4 pb-8">
				<svws-ui-notification type="warning">
					<template #header>
						Warning
					</template>
					<p>Lorem ipsum dolor sit, amet consectetur adipisicing elit.</p>
				</svws-ui-notification>
			</div>
		</Variant>
		<Variant title="Error" id="Error">
			<div class="p-4 pb-8">
				<svws-ui-notification type="error">
					<template #header>
						UserNotificationException
					</template>
					<p>Something went wrong. Das ist ein Beispiel für eine UserNotificationException.</p>
				</svws-ui-notification>
			</div>
		</Variant>
		<Variant title="Bug" id="Bug">
			<div class="p-4 pb-8">
				<svws-ui-notification type="bug" to-copy="DeveloperNotificationException">
					<template #header>
						DeveloperNotificationException
					</template>
					<p>Something went wrong. Das ist ein Beispiel für eine DeveloperNotificationException.</p>
					<template #stack>
						RouteManager@http://localhost:3000/src/router/RouteManager.ts?t=1677784265352:23:5
						create@http://localhost:3000/src/router/RouteManager.ts?t=1677784265352:46:30
						@http://localhost:3000/src/router/RouteManager.ts?t=1677784265352:196:43
					</template>
				</svws-ui-notification>
			</div>
		</Variant>
	</Story>
</template>

<script setup lang="ts">
</script>

<style>
html {
	font-size: 16px !important;
}
body {
	font-size: 0.875rem !important;
}
</style>
