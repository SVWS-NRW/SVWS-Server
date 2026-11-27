<template>
	<div class="flex h-full min-h-0 flex-col">
		<div class="shrink-0 text-headline">
			<slot name="title" />
		</div>
		<div v-if="$slots.header" class="shrink-0">
			<slot name="header" />
		</div>
		<div class="flex min-h-0 flex-1 flex-col [&>.svws-ui-table]:min-h-0 [&>.svws-ui-table-filter]:shrink-0">
			<slot />
		</div>
	</div>
</template>
