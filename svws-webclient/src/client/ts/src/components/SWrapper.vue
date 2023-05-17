<template>
	<router-view />
	<svws-ui-notifications v-if="errors.length">
		<template v-for="error of errors.reverse()" :key="error.message">
			<svws-ui-notification type="error">
				<template #header>
					{{ error.name }}
					<template v-if="error.name === 'DeveloperNotificationException'">
						<br>Programmierfehler: Bitte melden Sie diesen Fehler.
					</template>
					<template v-if="error.name === 'UserNotificationException'">
						<br>Nutzungsfehler: Dieser Fehler wurde durch eine nicht vorgesehene Nutzung der verwendeten Funktion hervorgerufen, z.B. durch unmögliche Kombinationen etc.
					</template>
				</template>
				{{ error.message }}
				<template #stack v-if="error.stack">
					<pre v-html="error.stack" />
				</template>
			</svws-ui-notification>
		</template>
	</svws-ui-notifications>
</template>

<script setup lang="ts">
	import { onErrorCaptured, ref } from 'vue';
	import { api } from '~/router/Api';

	const errors = ref<Error[]>([]);

	window.addEventListener("unhandledrejection", function (event) {
		api.status.stop();
		errors.value.push(new Error(event.reason))
		event.preventDefault();
	});

	onErrorCaptured((e) => {
		console.warn(e)
		errors.value.push(e);
		api.status.stop();
		return false;
	});

</script>
