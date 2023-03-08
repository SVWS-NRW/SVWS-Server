<template>
	<Story title="SVWS UI/Layout/Notification">
		<Variant title="Info">
			<Notification type="info">
				Info
			</Notification>
		</Variant>
		<Variant title="Error">
			<Notification type="error">
				<template #header>
					Error
				</template>
				<p>Something went wrong.</p>
				<template #stack>
					RouteManager@http://localhost:3000/src/router/RouteManager.ts?t=1677784265352:23:5
					create@http://localhost:3000/src/router/RouteManager.ts?t=1677784265352:46:30
					@http://localhost:3000/src/router/RouteManager.ts?t=1677784265352:196:43
				</template>
			</Notification>
			<div class="mb-1"></div>
			<Notification type="error">
				<p>Something went wrong.</p>
			</Notification>
		</Variant>
		<Variant title="Success">
			<Notification type="success">Success!</Notification>
		</Variant>
	</Story>
</template>
