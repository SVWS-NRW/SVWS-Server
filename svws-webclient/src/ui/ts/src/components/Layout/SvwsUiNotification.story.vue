<template>
	<Story title="SVWS UI/Layout/Notification">
		<Variant title="Info">
			<svws-ui-notification type="info">
				Info
			</svws-ui-notification>
		</Variant>
		<Variant title="Error & Bug">
			<svws-ui-notification type="bug">
				<template #header>
					Error
				</template>
				<p>Something went wrong. Das ist ein Beispiel für eine DeveloperNotificationException.</p>
				<template #stack>
					RouteManager@http://localhost:3000/src/router/RouteManager.ts?t=1677784265352:23:5
					create@http://localhost:3000/src/router/RouteManager.ts?t=1677784265352:46:30
					@http://localhost:3000/src/router/RouteManager.ts?t=1677784265352:196:43
				</template>
			</svws-ui-notification>
			<div class="mb-1" />
			<svws-ui-notification type="error">
				<p>Something went wrong. Das ist ein Beispiel für eine UserNotificationException.</p>
			</svws-ui-notification>
		</Variant>
		<Variant title="Success">
			<svws-ui-notification type="success">Success!</svws-ui-notification>
		</Variant>
	</Story>
</template>

<script setup lang="ts">
</script>
