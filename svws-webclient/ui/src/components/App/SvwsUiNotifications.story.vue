<template>
	<Story title="Notifications" id="svws-ui-notifications" group="app" :layout="{type: 'grid', width: '45%'}" icon="ri:notification-badge-line">
		<Variant title="Success" id="Success">
			<svws-ui-notification type="success">
				<template #header>
					Success
				</template>
				<p>Das hat gut geklappt!</p>
			</svws-ui-notification>
		</Variant>
		<Variant title="Info" id="Info">
			<svws-ui-notification type="info">
				Info
			</svws-ui-notification>
		</Variant>
		<Variant title="Error" id="Error">
			<svws-ui-notification type="error">
				<template #header>
					UserNotificationException
				</template>
				<p>Something went wrong. Das ist ein Beispiel für eine UserNotificationException.</p>
			</svws-ui-notification>
		</Variant>
		<Variant title="Bug" id="Bug">
			<svws-ui-notification type="bug">
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
