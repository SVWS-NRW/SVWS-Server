<template>
	<svws-ui-app-layout>
		<template #sidebar>
			<svws-ui-menu>
				<svws-ui-menu-item :active="false" @click="goBack">
					<template #label>
						Zurück
					</template>
					<template #icon>
						<i-ri-arrow-go-back-line />
					</template>
				</svws-ui-menu-item>
				<svws-ui-menu-item :active="false" @click="reloadClient">
					<template #label>
						Neu laden
					</template>
					<template #icon>
						<i-ri-restart-line />
					</template>
				</svws-ui-menu-item>
			</svws-ui-menu>
		</template>
		<template #main>
			<div class="app--page">
				<svws-ui-header>
					<div class="flex items-center">
						<div>
							<span class="inline-flex gap-2"><i-ri-alert-fill class="text-error" />{{ error?.name }}</span>
							<br>
							<span class="opacity-40">
								<template v-if="code !== undefined">
									Fehler {{ code }}
								</template>
								<template v-else>
									Unbekannter Fehlercode
								</template>
							</span>
						</div>
					</div>
				</svws-ui-header>
				<div class="svws-ui-page" v-if="error !== undefined">
					<div class="svws-ui-tab-content">
						<div class="page--content">
							<svws-ui-content-card :title="error?.message">
								<pre>{{ error.stack }}</pre>
							</svws-ui-content-card>
						</div>
					</div>
				</div>
			</div>
		</template>
	</svws-ui-app-layout>
</template>


<script setup lang="ts">

	import type { ErrorProps } from "./SErrorProps";

	const props = defineProps<ErrorProps>();

	function goBack() {
		window.history.back();
	}

	function reloadClient() {
		window.location.href = window.location.origin;
	}

</script>
